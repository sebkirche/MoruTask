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
package morutask.GUI.threads.reminder;

import morutask.GUI.utils.MTSwingUtilities;
import morutask.models.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import morutask.GUI.Timer.*;

/**
 *
 * @author mohand
 */
public class ReminderDialog extends JDialog{
    
    private JLabel titleLabel;
    private JButton doneButton;
    private JButton snoozeButton;
    private JButton DismissButton;
    private Task task;
    
    private static ReminderDialog instance;
    
    private ReminderDialog()
    {
        this.initialize();
    }
    
    public static ReminderDialog getInstance()
    {
        if (instance == null)
            instance = new ReminderDialog();
        
        return instance;
    }
    
    private void initialize()
    {
        setTitle("Reminder");
        setSize(300, 150);
        setVisible(true);
        setLayout(new BorderLayout(5, 5));
        
        titleLabel = new JLabel();
        
        doneButton = new JButton("Done");
        doneButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                task.setCompleted(true);
                setVisible(false);
                showtimer();
            }
        });
        
        DismissButton = new JButton("Dismiss");
        DismissButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        
        snoozeButton = new JButton("Snooze");
        
        add(titleLabel,BorderLayout.CENTER);
        
        JPanel panel = new JPanel(new FlowLayout(5, 5, 5));
        
        panel.add(doneButton);
        panel.add(DismissButton);
        panel.add(snoozeButton);
        
        add(panel,BorderLayout.PAGE_END);
        
    }
    
    public void viewTask(Task t)
    {
        this.task = t;
        titleLabel.setText(t.getTitle());
        setVisible(true);
    }

    public void showtimer()
    {
        if(task.getTimer().getValue() !=0)
        {
            System.out.println("Timer is started");
            //task.getTimer().start();
                MTSwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            TimerTrayIcon timer = new TimerTrayIcon(task);
                        } catch (AWTException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                });
                //TimerTrayIcon timer = new TimerTrayIcon(task);
        }
    }
}
