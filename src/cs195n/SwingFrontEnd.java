package cs195n;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.UIManager;

/**
 * An implementation of {@link CS195NFrontEnd} that uses Swing/AWT for drawing
 * and events. <br>
 * <br>
 * To use, create a subclass that overrides the various <code>on</code>***
 * methods of CS195NFrontEnd and call {@link #startup()} to begin processing
 * events.
 * 
 * @author zdavis
 */
public abstract class SwingFrontEnd extends CS195NFrontEnd {
	
	@SuppressWarnings("serial")
	private class DrawPanel extends JComponent implements MouseListener, MouseMotionListener, MouseWheelListener,
			KeyListener, ComponentListener, KeyEventDispatcher {
		
		private class RealReleaseWaiter implements ActionListener {
			
			private boolean			cancelled	= false;
			private final KeyEvent	evt;
			private final Timer		t;
			
			public RealReleaseWaiter(KeyEvent evt) {
				this.evt = evt;
				t = new Timer(SwingFrontEnd.MILLIS_TO_WAIT_FOR_REPEAT, this);
				t.start();
			}
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (cancelled) return;
				cancel();
				keyReleased(evt);
			}
			
			public void cancel() {
				cancelled = true;
				t.stop();
				waiters.remove(this);
			}
			
			public int code() {
				return evt.getKeyCode();
			}
			
		}
		
		private static final long			serialVersionUID	= 7488280726456603287L;
		
		boolean								resizeNotCalled		= true;
		
		java.util.List<RealReleaseWaiter>	waiters				= new ArrayList<RealReleaseWaiter>();
		
		public DrawPanel() {
			setDoubleBuffered(true);
			setOpaque(true);
		}
		
		public void callOnResize(int width, int height) {
			resizeNotCalled = false;
			final Vec2i newSize = new Vec2i(width, height);
			getFrame().setBackground(Color.black);
			clientSize = newSize;
			if (SwingFrontEnd.this.debug) updateTitle();
			if (!SwingFrontEnd.this.fullscreen) SwingFrontEnd.this.windowedSize = newSize;
			try {
				onResize(newSize);
			} catch (final Throwable t) {
				throwableGenerated("onResize", t);
			}
		}
		
		@Override
		public void componentHidden(ComponentEvent e) {
			// no-op
		}
		
		@Override
		public void componentMoved(ComponentEvent e) {
			// no-op
		}
		
		@Override
		public void componentResized(ComponentEvent e) {
			final Dimension d = e.getComponent().getSize();
			callOnResize(d.width, d.height);
		}
		
		@Override
		public void componentShown(ComponentEvent e) {
			// no-op
		}
		
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if (getFrame().isFocused() && !e.isConsumed()) switch (e.getID()) {
			case KeyEvent.KEY_PRESSED:
				keyPressed(e);
				return true;
			case KeyEvent.KEY_RELEASED:
				queueKeyReleased(e);
				return true;
			case KeyEvent.KEY_TYPED:
				keyTyped(e);
				return true;
			}
			return false;
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			notifyKeyPress(e);
			if (SwingFrontEnd.this.debug) if (e.getKeyCode() == KeyEvent.VK_F11) {
				openResizeDialog();
				return;
			}
			try {
				onKeyPressed(e);
			} catch (final Throwable t) {
				throwableGenerated("onKeyPressed", t);
			}
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			if (SwingFrontEnd.this.debug) switch (e.getKeyCode()) {
			case KeyEvent.VK_F11:
				return;
			default:
				break;
			}
			try {
				onKeyReleased(e);
			} catch (final Throwable t) {
				throwableGenerated("onKeyReleased", t);
			}
		}
		
		@Override
		public void keyTyped(KeyEvent e) {
			try {
				onKeyTyped(e);
			} catch (final Throwable t) {
				throwableGenerated("onKeyTyped", t);
			}
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			try {
				onMouseClicked(e);
			} catch (final Throwable t) {
				throwableGenerated("onMouseClicked", t);
			}
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			try {
				onMouseDragged(e);
			} catch (final Throwable t) {
				throwableGenerated("onMouseDragged", t);
			}
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			// no-op
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			// no-op
		}
		
		@Override
		public void mouseMoved(MouseEvent e) {
			try {
				onMouseMoved(e);
			} catch (final Exception ex) {
				ex.printStackTrace();
			}
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			try {
				onMousePressed(e);
			} catch (final Throwable t) {
				throwableGenerated("onMousePressed", t);
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			try {
				onMouseReleased(e);
			} catch (final Throwable t) {
				throwableGenerated("onMouseReleased", t);
			}
		}
		
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			try {
				onMouseWheelMoved(e);
			} catch (final Throwable t) {
				throwableGenerated("onMouseWheelMoved", t);
			}
		}
		
		private void notifyKeyPress(KeyEvent e) {
			// iterate over indices for performance... I consider 3-4 millis
			// "time sensitive"
			final int size = waiters.size(), code = e.getKeyCode();
			for (int i = 0; i < size; ++i) {
				final RealReleaseWaiter waiter = waiters.get(i);
				if (code == waiter.code()) waiter.cancel();
			}
		}
		
		/*
		 * Fix X being dumb and sending keyrelease on every key repeat.
		 * Optimized+generalized version of poster Ekipur's solution at:
		 * http://bugs.sun.com/view_bug.do?bug_id=4153069
		 */
		
		@Override
		public void paint(Graphics g) {
			final Rectangle r = g.getClipBounds();
			g.clearRect(r.x, r.y, r.width, r.height);
			if (resizeNotCalled) callOnResize(r.width, r.height);
			try {
				onDraw((Graphics2D) g);
			} catch (final Throwable t) {
				throwableGenerated("onDraw", t);
			}
		}
		
		private void queueKeyReleased(KeyEvent e) {
			waiters.add(new RealReleaseWaiter(e));
		}
		
		void startListening() {
			addMouseListener(this);
			addMouseMotionListener(this);
			addMouseWheelListener(this);
			addComponentListener(this);
			KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
		}
		
		void stopListening() {
			removeMouseListener(this);
			removeMouseMotionListener(this);
			removeMouseWheelListener(this);
			removeComponentListener(this);
			KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
		}
	}
	
	/**
	 * Amount of time between ticks. A value of "1000 / N" means about N frames
	 * per second
	 */
	private static final int	DEFAULT_DELAY_MILLIS		= 1000 / 75;
	/**
	 * Number of milliseconds to wait for another keyPressed before dispatching
	 * a keyReleased
	 */
	private static final int	MILLIS_TO_WAIT_FOR_REPEAT	= 5;
	
	/**
	 * Number of frames to average in FPS count
	 */
	private static final int	NUM_FRAMES_TO_AVERAGE		= 10;
	
	static {
		// try to set look-and-feel to current platform
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (final Throwable t) {
			// too bad
		}
	}
	
	public static void enableFullScreenMode(Window window) {
		final String className = "com.apple.eawt.FullScreenUtilities";
		final String methodName = "setWindowCanFullScreen";
		
		try {
			final Class<?> clazz = Class.forName(className);
			final Method method = clazz.getMethod(methodName, new Class<?>[] { Window.class, boolean.class });
			method.invoke(null, window, true);
		} catch (final Throwable t) {
			System.err.println("Full screen mode is not supported");
			t.printStackTrace();
		}
	}
	
	private Vec2i			clientSize;
	private final int		closeOp;
	private JFrame			frame;
	
	private Point			framePos;
	private long			lastTickNanos;
	
	private final DrawPanel	panel;
	private final Pattern	resizePattern	= Pattern.compile("[^0-9]*([0-9]+)[^0-9]+([0-9]+)");
	
	private final long[]	tickTimes		= new long[SwingFrontEnd.NUM_FRAMES_TO_AVERAGE];
	
	private int				tickTimesIndex	= -1;
	
	private final Timer		timer;
	
	private final String	title;
	
	/**
	 * Creates the front-end with a default window size. Note that events will
	 * not begin until {@link #startup()} is called.
	 * 
	 * @param title the title of the window
	 * @param fullscreen true for fullscreen, false for windowed
	 */
	public SwingFrontEnd(String title, boolean fullscreen) {
		this(title, fullscreen, CS195NFrontEnd.DEFAULT_WINDOW_SIZE);
	}
	
	/**
	 * Creates the front-end. Note that events will not begin until
	 * {@link #startup()} is called.
	 * 
	 * @param title the title of the window
	 * @param fullscreen true for fullscreen, false for windowed
	 * @param windowSize the starting size of the window
	 */
	public SwingFrontEnd(String title, boolean fullscreen, Vec2i windowSize) {
		this(title, fullscreen, windowSize, JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Creates the front-end. Note that events will not begin until
	 * {@link #startup()} is called.
	 * 
	 * @param title the title of the window
	 * @param fullscreen true for fullscreen, false for windowed
	 * @param windowSize the starting size of the window
	 * @param closeOp argument to be passed to
	 *            {@link JFrame#setDefaultCloseOperation(int)}.
	 */
	public SwingFrontEnd(String title, boolean fullscreen, Vec2i windowSize, int closeOp) {
		super(fullscreen, windowSize);
		this.title = title;
		this.closeOp = closeOp;
		clientSize = windowSize;
		
		panel = new DrawPanel();
		panel.setMinimumSize(new Dimension(CS195NFrontEnd.MINIMUM_WINDOW_SIZE.x, CS195NFrontEnd.MINIMUM_WINDOW_SIZE.y));
		
		timer = new Timer(SwingFrontEnd.DEFAULT_DELAY_MILLIS, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				doTick();
			}
		});
	}
	
	@Override
	final void doSetDebugMode() {
		updateTitle();
	}
	
	@Override
	final void doSetFullscreen() {
		if (frame != null) {
			if (fullscreen) framePos = frame.getLocation();
			panel.stopListening();
			frame.remove(panel);
			frame.dispose();
		}
		
		frame = new JFrame();
		SwingFrontEnd.enableFullScreenMode(frame);
		frame.setDefaultCloseOperation(closeOp);
		frame.setLayout(new BorderLayout());
		frame.add(panel, BorderLayout.CENTER);
		
		frame.setUndecorated(fullscreen);
		if (fullscreen) {
			final Rectangle bounds = frame.getGraphicsConfiguration().getBounds();
			frame.setBounds(bounds);
		} else {
			// uncomment to make minimum window size the minimum size we'll test
			panel.setPreferredSize(new Dimension(CS195NFrontEnd.MINIMUM_WINDOW_SIZE.x,
					CS195NFrontEnd.MINIMUM_WINDOW_SIZE.y));
			frame.pack();
			frame.setMinimumSize(frame.getSize());
			
			panel.setPreferredSize(new Dimension(windowedSize.x, windowedSize.y));
			frame.pack();
			
			// workaround for department jvm install being buggy
			panel.callOnResize(panel.getSize().width, panel.getSize().height);
			
			if (framePos == null)
				frame.setLocationRelativeTo(null);
			else
				frame.setLocation(framePos);
		}
		
		updateTitle();
		panel.startListening();
		
		if (running) frame.setVisible(true);
	}
	
	@Override
	final void doSetTickFrequency(long nanoDelay) {
		final int milliDelay = (int) (nanoDelay / 1000000);
		timer.setDelay(milliDelay);
		timer.setInitialDelay(milliDelay);
	}
	
	@Override
	final void doShutdown() {
		timer.stop();
		panel.stopListening();
		frame.remove(panel);
		frame.dispose();
		frame = null;
	}
	
	@Override
	final void doStartup() {
		doSetFullscreen();
		timer.start();
		lastTickNanos = System.nanoTime();
	}
	
	final void doTick() {
		final long currentNanos = System.nanoTime();
		final long delta = currentNanos - lastTickNanos;
		
		tickTimes[tickTimesIndex = (tickTimesIndex + 1) % SwingFrontEnd.NUM_FRAMES_TO_AVERAGE] = delta;
		if (debug) updateTitle();
		
		try {
			onTick(delta);
		} catch (final Throwable t) {
			throwableGenerated("onTick", t);
		}
		panel.repaint();
		lastTickNanos = currentNanos;
	}
	
	private float getFPS() {
		long sum = 0;
		for (final long val : tickTimes)
			sum += val;
		return (1000000000.f * SwingFrontEnd.NUM_FRAMES_TO_AVERAGE) / sum;
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	private void openResizeDialog() {
		if (fullscreen) return;
		
		final String result = JOptionPane.showInputDialog(frame,
				"Please enter the exact width and height you want to test", "Resize", JOptionPane.QUESTION_MESSAGE);
		
		if (result == null) return;
		
		final Matcher m = resizePattern.matcher(result);
		if (m.lookingAt()) try {
			final int width = Integer.valueOf(m.group(1));
			final int height = Integer.valueOf(m.group(2));
			panel.setPreferredSize(new Dimension(width, height));
			frame.pack();
			return;
		} catch (final NumberFormatException e) {
			// Only gets here on error, do the message dialog
		}
		
		JOptionPane.showMessageDialog(frame, "Could not parse width and/or height from input", "Error",
				JOptionPane.ERROR_MESSAGE);
	}
	
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
	
	private synchronized void throwableGenerated(String method, Throwable t) {
		System.out.flush();
		try {
			Thread.sleep(50);
		} catch (final InterruptedException e) {
			// Doesn't matter if interrupted
		}
		System.err.flush();
		t.printStackTrace();
		System.err.println("\n***An uncaught " + t.getClass().getSimpleName() + " (thrown in "
				+ getClass().getSimpleName() + "." + method
				+ ") propagated up to the front-end, check above stack trace for details***");
		System.err.flush();
		System.exit(1);
	}
	
	private void updateTitle() {
		if (frame != null) {
			String title;
			if (debug)
				title = String.format("%s [size= %s, aspect= %.6f:1, FPS= %.3f]", this.title, clientSize,
						((float) clientSize.x / (float) clientSize.y), getFPS());
			else
				title = this.title;
			frame.setTitle(title);
		}
	}
}
