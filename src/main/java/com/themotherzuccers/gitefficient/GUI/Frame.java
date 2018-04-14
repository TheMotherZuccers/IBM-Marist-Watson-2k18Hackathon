package com.themotherzuccers.gitefficient.GUI;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class Frame {
    import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

    public class FrameTest2 extends JPanel
            implements ActionListener, FocusListener {

        JTextField usernameField;
        boolean usernameSet = false;
        Font regularFont, italicFont;
        JLabel usernameDisplay;
        final static int GAP = 10;

        public FrameTest2() {
            super(new BorderLayout());
            setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

            JPanel leftHalf = new JPanel() {
                //Don't allow us to stretch vertically.
                public Dimension getMaximumSize() {
                    Dimension pref = getPreferredSize();
                    return new Dimension(Integer.MAX_VALUE,
                            pref.height);
                }
            };
            leftHalf.setLayout(new BoxLayout(leftHalf,
                    BoxLayout.PAGE_AXIS));
            leftHalf.add(createEntryFields());
            leftHalf.add(createButtons());

            add(leftHalf);
            add(createUsernameDisplay());
        }



        public void actionPerformed(ActionEvent e) {
            if ("clear".equals(e.getActionCommand())) {
                usernameSet = false;
                usernameField.setText("");

            }
            else {
                usernameSet = true;
            }
            updateDisplays();
        }


        protected JComponent createButtons(){
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

            JButton button = new JButton("Set username");
            button.addActionListener(this);
            panel.add(button);

            button = new JButton("Clear username");
            button.addActionListener(this);
            button.setActionCommand("clear");
            panel.add(button);

            //Match the SpringLayout's gap, subtracting 5 to make
            //up for the default gap FlowLayout provides.
            panel.setBorder(BorderFactory.createEmptyBorder(0, 0,
                    GAP-5, GAP-5));
            return panel;
        }



        protected void updateDisplays() {
            usernameDisplay.setText(formatUsername());
            if (usernameSet) {
                usernameDisplay.setFont(regularFont);
            } else {
                usernameDisplay.setFont(italicFont);
            }
        }

        protected JComponent createUsernameDisplay() {
            JPanel panel = new JPanel(new BorderLayout());
            usernameDisplay = new JLabel();
            usernameDisplay.setHorizontalAlignment(JLabel.CENTER);
            regularFont = usernameDisplay.getFont().deriveFont(Font.PLAIN,
                    16.0f);
            italicFont = regularFont.deriveFont(Font.ITALIC);
            updateDisplays();

            //Lay out the panel.
            panel.setBorder(BorderFactory.createEmptyBorder(
                    GAP/2, //top
                    0,     //left
                    GAP/2, //bottom
                    0));   //right
            panel.add(new JSeparator(JSeparator.VERTICAL),
                    BorderLayout.LINE_START);
            panel.add(usernameDisplay,
                    BorderLayout.CENTER);
            panel.setPreferredSize(new Dimension(900, 650));

            return panel;
        }

        protected String formatUsername() {
            if (!usernameSet) return "No username set.";

            String user = usernameField.getText();

            String empty = "";

            if ((user == null) || empty.equals(user)) {
                user = "<em>(no username specified)</em>";
            }

            StringBuffer sb = new StringBuffer();
            sb.append("<html><p align=center>");
            sb.append(user);
            sb.append("</p></html>");

            return sb.toString();
        }

        //A convenience method for creating a MaskFormatter.
        protected MaskFormatter createFormatter(String s) {
            MaskFormatter formatter = null;
            try {
                formatter = new MaskFormatter(s);
            } catch (java.text.ParseException exc) {
                System.err.println("formatter is bad: " + exc.getMessage());
                System.exit(-1);
            }
            return formatter;
        }

        //Needed for FocusListener interface.
        public void focusLost(FocusEvent e) { } //ignore
        public void focusGained(FocusEvent e) { } //ignore

        protected JComponent createEntryFields() {
            JPanel panel = new JPanel(new SpringLayout());

            String[] labelStrings = {
                    "Username: ",
            };

            JLabel[] labels = new JLabel[labelStrings.length];
            JComponent[] fields = new JComponent[labelStrings.length];
            int fieldNum = 0;

            //Create the text field and set it up.
            usernameField  = new JTextField();
            usernameField.setColumns(20);
            fields[fieldNum++] = usernameField;

            //Associate label/field pairs, add everything,
            //and lay it out.
            for (int i = 0; i < labelStrings.length; i++) {
                labels[i] = new JLabel(labelStrings[i],
                        JLabel.TRAILING);
                labels[i].setLabelFor(fields[i]);
                panel.add(labels[i]);
                panel.add(fields[i]);

                //Add listeners to each field.
                JTextField tf = null;
                if (fields[i] instanceof JSpinner) {
                    tf = getTextField((JSpinner)fields[i]);
                } else {
                    tf = (JTextField)fields[i];
                }
                tf.addActionListener(this);
                tf.addFocusListener(this);
            }
            SpringUtilities.makeCompactGrid(panel,
                    labelStrings.length, 2,
                    GAP, GAP, //init x,y
                    GAP, GAP/2);//xpad, ypad
            return panel;
        }

        public JFormattedTextField getTextField(JSpinner spinner) {
            JComponent editor = spinner.getEditor();
            if (editor instanceof JSpinner.DefaultEditor) {
                return ((JSpinner.DefaultEditor)editor).getTextField();
            } else {
                System.err.println("Unexpected editor type: "
                        + spinner.getEditor().getClass()
                        + " isn't a descendant of DefaultEditor");
                return null;
            }
        }

        public void drawing(){
            repaint();
        }

        public void paintComponent(Graphics g){
            super.paintComponent(g);

            g.setColor(Color.RED);
            g.fillRect(10, 100, 10, 50);

        }

        /**
         * Create the GUI and show it.  For thread safety,
         * this method should be invoked from the
         * event dispatch thread.
         */
        private static void createAndShowGUI() {
            //Create and set up the window.
            JFrame frame = new JFrame("TextInputDemo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JLabel emptyLabel = new JLabel("");
            emptyLabel.setPreferredSize(new Dimension(950, 600));
            frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);

            //Add contents to the window.
            Draw rect1 = new Draw();
            frame.add(rect1);
            rect1.drawing();
            frame.add(new Frame());



            //Display the window.
            frame.pack();
            frame.setVisible(true);
        }


        public static void main(String[] args) {
            //Schedule a job for the event dispatch thread:
            //creating and showing this application's GUI.
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    //Turn off metal's use of bold fonts
                    UIManager.put("swing.boldMetal", Boolean.FALSE);
                    createAndShowGUI();
                }
            });
        }


    }


}
