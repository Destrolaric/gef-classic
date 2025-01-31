/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.edit;

import java.beans.PropertyChangeEvent;

import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;

import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;
import org.eclipse.gef.examples.logicdesigner.figures.LabelFigure;
import org.eclipse.gef.examples.logicdesigner.model.LogicLabel;

public class LogicLabelEditPart extends LogicEditPart {

	@Override
	protected AccessibleEditPart createAccessible() {
		return new AccessibleGraphicalEditPart() {
			@Override
			public void getValue(AccessibleControlEvent e) {
				e.result = getLogicLabel().getLabelContents();
			}

			@Override
			public void getName(AccessibleEvent e) {
				e.result = LogicMessages.LogicPlugin_Tool_CreationTool_LogicLabel;
			}
		};
	}

	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, null);
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new LabelDirectEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new LogicLabelEditPolicy());
	}

	@Override
	protected IFigure createFigure() {
		LabelFigure label = new LabelFigure();
		return label;
	}

	private LogicLabel getLogicLabel() {
		return (LogicLabel) getModel();
	}

	private void performDirectEdit() {
		new LogicLabelEditManager(this, new LabelCellEditorLocator((LabelFigure) getFigure())).show();
	}

	@Override
	public void performRequest(Request request) {
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT)
			performDirectEdit();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equalsIgnoreCase("labelContents"))//$NON-NLS-1$
			refreshVisuals();
		else
			super.propertyChange(evt);
	}

	@Override
	protected void refreshVisuals() {
		((LabelFigure) getFigure()).setText(getLogicLabel().getLabelContents());
		super.refreshVisuals();
	}

}
