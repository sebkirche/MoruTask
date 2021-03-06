/*
 * MoruTask
 * Copyright (c) 2013, Mohand Andel
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of MoruTask or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package morutask.GUI.Table;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.table.AbstractTableModel;

import morutask.GUI.utils.viewUtils;
import morutask.models.Task;
import morutask.models.TaskFactory;

/**
 *
 * @author mohand
 */
public class modelTable extends AbstractTableModel implements ListChangeListener , PropertyChangeListener{

    private static boolean edited;
    public modelTable()
    {
        edited = false;
        TaskFactory.getInstance().addListChangeListener(this);
        TaskFactory.getInstance().addPropertyChangeListener(this);
    }

    @Override
    public int getRowCount() {

       return TaskFactory.getInstance().size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        
        return (Task)TaskFactory.getInstance().get(rowIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Task.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

            if (isEdited() )//|| viewUtils.getInstance().getTaskTable().getSelectedRow()==rowIndex)
                return true;
        return false;

    }

    public static boolean isEdited() {
        return edited;
    }

    public static void setEdited(boolean edited) {
        modelTable.edited = edited;
    }

    @Override
    public void listChange(ListChangeEvent lce) {
        
        if (lce.getChangeType() == ListChangeEvent.VALUE_ADDED) {
             System.out.println("Added index = " + lce.getValue());
           
             fireTableRowsInserted(lce.getIndex(), lce.getIndex());
        }
        if (lce.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
             System.out.println("Removed");
             fireTableRowsDeleted(lce.getIndex(), lce.getIndex());
        }
        if (lce.getChangeType() == ListChangeEvent.VALUE_CHANGED) {
            System.out.println("listChanged");
        }
        
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        
        
        if ( evt.getSource() instanceof Task)
        {
             //fireTableDataChanged();
            fireTableCellUpdated(viewUtils.getInstance().getTaskTable().getSelectedRow(),0);
        }
        
    }
    
}
