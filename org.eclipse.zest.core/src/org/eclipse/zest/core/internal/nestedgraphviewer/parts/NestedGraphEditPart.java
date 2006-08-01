/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.nestedgraphviewer.parts;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.gef.EditPolicy;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphItem;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedGraphModel;
import org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphEditPart;
import org.eclipse.mylar.zest.core.internal.nestedgraphviewer.policies.NestedGraphRootLayoutEditPolicy;


/**
 * Extends GraphEditPart to allow moving and resizing of nodes.
 * 
 * @author Chris Callendar
 */
public class NestedGraphEditPart extends GraphEditPart  {
	
	/**
	 * Initializes the edit part.
	 * @param allowOverlap If nodes are allowed to overlap
	 * @param enforceBounds If nodes can be moved outside the bounds  If this is set to false
	 * then scrollbars will appear.
	 */
	public NestedGraphEditPart( ) {
		super();
		
	}	
	
	/**
	 * Upon activation, attach to the model element as a property change listener.
	 */
	public void activate() {
		if (!isActive()) {
			super.activate();
			((GraphItem)getCastedModel().getRootNode()).addPropertyChangeListener(this);
		}
	}	
	
	/**
	 * Upon deactivation, detach from the model element as a property change listener.
	 */
	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			((GraphItem)getCastedModel().getRootNode()).removePropertyChangeListener(this);
		}
	}	

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.SimpleRootEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		//@tag bug(152393-TopSelection(fix)) : add an edit policy to the EditPart which will create policies that don't allow the nodes to move.
		super.createEditPolicies();
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new NestedGraphRootLayoutEditPolicy());
	}
	
	//TODO: should we change this to getNestedGraphModel?
	// and make the same method in GraphEditPart called getGraphModel?
	public NestedGraphModel getCastedModel() {
		return (NestedGraphModel)getModel();
	}	
	

	/**
	 * Resize the single figure on this layer and the 1 figure of the current selected nod
	 * @param width
	 * @param height
	 */
	public void resize( int width, int height ) {
		getFigure().setSize(width, height);
		IFigure freeformLayer = (IFigure)((NestedGraphNodeEditPart)getCastedModel().getRootNode().getEditPart()).getFigure();
		freeformLayer = (IFigure)((NestedGraphNodeEditPart)getCastedModel().getCurrentNode().getEditPart()).getFigure();
		freeformLayer.setSize(width, height);	
	}
	
	/**
	 * Creates a NestedFreeformLayer which contains the root NestedFigure.
	 * This NestedFigure will have an up button in the top left if the
	 * current node isn't the root node.
	 */
	protected IFigure createFigure() {	
		Figure figure = new FreeformLayer();
		figure.addLayoutListener(LayoutAnimator.getDefault());
		figure.setOpaque(true);
		return figure;
	}		
}
