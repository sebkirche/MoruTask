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
package morutask.GUI;

import com.leclercb.commons.api.coder.exc.FactoryCoderException;
import morutask.GUI.Actions.ActionAddTask;
import morutask.GUI.Actions.ActionDeleteTask;
import morutask.GUI.List.GroupList;
import morutask.GUI.List.items.itemUnit;
import morutask.GUI.Sort.TaskComparable;
import morutask.GUI.Sort.TaskSortType;
import morutask.GUI.Table.Filter.TaskFilter;
import morutask.GUI.Table.TaskTable;
import morutask.GUI.utils.ComponentFactory;
import morutask.GUI.utils.FormBuildHelper;
import morutask.GUI.utils.viewUtils;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXSearchField;
import org.jdesktop.swingx.sort.TableSortController;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mohand
 */
public class MainFrame extends JXFrame{
    
    private JXSearchField searchField;
    private JComboBox sortComboBox;
    private JButton addButton;
    private JButton deleteButton;
    private JScrollPane scrollPane;
    private GroupList groupList;
    private TaskTable taskTable;
    private JPanel panel;
    private TableSortController rowsorter;
    private FormBuildHelper formBuildHelper;
    private JSplitPane splitPane;
    //private TasksCalendarPanel tasksCalendarPanel;


    public MainFrame()
    {
        this.initialize();


//        final JTabbedPane tabbedPane = new JTabbedPane();
//
//        tabbedPane.addTab("Standard View", splitPane);
//        tabbedPane.addTab("Calendar View",tasksCalendarPanel);
//
//        tabbedPane.addChangeListener(new ChangeListener() {
//            @Override
//            public void stateChanged(ChangeEvent changeEvent) {
//
//                if(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex())=="Standard View")
//
//                     viewUtils.getInstance().setCurrentViewType(viewUtils.ViewType_Task);
//
//                else
//                    viewUtils.getInstance().setCurrentViewType(viewUtils.ViewType_Calendar);
//
//                System.out.println(viewUtils.getInstance().getCurrentViewType());
//            }
//        });

        //panel.add(tabbedPane,BorderLayout.CENTER);
        getContentPane().add(splitPane,BorderLayout.CENTER);

    }
    
    private void initialize()
    {
        setLayout(new BorderLayout(10, 10));
        setSize(950, 550);

        setTitle("MoruTask");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    System.out.println("Saving Models...");
                    Main.saveModels();
                    //Main.saveSettings();
                } catch (FileNotFoundException | FactoryCoderException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        formBuildHelper = new FormBuildHelper();
        
        scrollPane = new JScrollPane();
        
        groupList = new GroupList();
        groupList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                itemUnit selectedValue = (itemUnit) groupList.getSelectedValue();
                TaskFilter.setFilter(selectedValue.getFilter());
                rowsorter.sort();
            }
        });
        taskTable = new TaskTable();
        
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setViewportView(taskTable);
        
        rowsorter = (TableSortController) viewUtils.getInstance().getTaskTable().getRowSorter();

        splitPane = ComponentFactory.createThinJSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setRightComponent(scrollPane);
        splitPane.setLeftComponent(groupList);
        splitPane.setDividerSize(10);
        splitPane.setOneTouchExpandable(true);

        searchField = new JXSearchField("Search");
        searchField.setColumns(20);
        searchField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (searchField.getText().length() ==0)
                        rowsorter.setRowFilter(new TaskFilter());
                else
                    rowsorter.setRowFilter(RowFilter.regexFilter(searchField.getText()));
            }
            
});
        searchField.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                JTextField source = (JTextField) e.getSource();
                if (source.getText().equalsIgnoreCase("Search"))
                {
                    source.setText(null);
                }
            }
            
});
        
        sortComboBox = new JComboBox(new DefaultComboBoxModel<>(TaskSortType.values()));
        sortComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                
               if (e.getStateChange() == ItemEvent.SELECTED)
               {
                   //TableSortController rowsorter = (TableSortController) viewUtils.getInstance().getTaskTable().getRowSorter();
                   TaskComparable taskComparable = new TaskComparable();
                   taskComparable.setTypeOfComparison((TaskSortType) sortComboBox.getSelectedItem());
                   
                   viewUtils.getInstance().getTaskTable().resetSortOrder();
                   rowsorter.setComparator(0, taskComparable);
                   viewUtils.getInstance().getTaskTable().setSortOrder(0, SortOrder.DESCENDING);
               } 
            }
        });
        
        addButton = new JButton("Add");
        addButton.addActionListener(new ActionAddTask());
        
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionDeleteTask());
    
        
        panel = new JPanel(new FlowLayout(5, 5, 5));
        panel.add(addButton);
        panel.add(deleteButton);
        panel.add(formBuildHelper.appendLabeltoComponent("Sort", sortComboBox, true));
        panel.add(searchField);
        
        add(panel,BorderLayout.PAGE_START);
         add(splitPane,BorderLayout.CENTER);
         //tasksCalendarPanel = new TasksCalendarPanel();
        //add(tasksCalendarPanel,BorderLayout.CENTER);
        //add(groupList,BorderLayout.LINE_START);
        //add(scrollPane,BorderLayout.CENTER);
        
    }
    
}
