/*******************************************************************************
 * Copyright (c) 2000, 2023 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.test;

import java.util.Map;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionEndpointLocator;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.UpdateListener;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ConnectionEndPointMoveTest extends BaseTestCase implements UpdateListener {

	private IFigure contents;
	private RectangleFigure dec;
	private Shell shell;
	private PolylineConnection conn;
	private Rectangle lastDamaged;
	private Rectangle origBounds;

	/*
	 * @see TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		shell = new Shell(Display.getDefault());

		String appName = getClass().getName();
		appName = appName.substring(appName.lastIndexOf('.') + 1);

		shell.setText(appName);
		shell.setLayout(new FillLayout());

		FigureCanvas fc = new FigureCanvas(shell);

		fc.setSize(200, 200);

		contents = new Figure();

		conn = new PolylineConnection();

		dec = new RectangleFigure();

		dec.setBounds(new Rectangle(10, 10, 25, 25));

		RectangleFigure node = new RectangleFigure();
		conn.setTargetAnchor(new ChopboxAnchor(node));
		conn.add(dec, new ConnectionEndpointLocator(conn, true));

		conn.setStart(new Point(25, 25));
		conn.setEnd(new Point(125, 125));
		contents.add(conn);

		fc.setContents(contents);

		shell.open();
		performUpdate();
		contents.getUpdateManager().addUpdateListener(this);

		origBounds = conn.getBounds();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.UpdateListener#notifyPainting(org.eclipse.draw2d.geometry
	 * .Rectangle, java.util.Map)
	 */
	@Override
	public void notifyPainting(Rectangle damage, Map dirtyRegions) {
		lastDamaged = damage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.UpdateListener#notifyValidating()
	 */
	@Override
	public void notifyValidating() {
		// nothing
	}

	public void testConnectionDecoration() {
		conn.setConstraint(dec, new MidpointLocator(conn, 0));
		conn.layout();

		performUpdate();
		assertTrue(lastDamaged.contains(origBounds));
	}

	/*
	 * @see TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		shell.dispose();
	}

	private void performUpdate() {
		contents.getUpdateManager().performUpdate();
		waitEventLoop(shell, 100);
	}

}
