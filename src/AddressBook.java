/**
File: AddressBook.java
Description: This method will add an address object to a list.  
It will also open a file and append the address to a comma delimited line. 
Append the record so that a running list will be kept in the address book file.
Author: Jonathan Villegas
mail: jonathan.e.villegas@gmail.com
Date: 5/18/14
**/

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
/**
 * AddressBook.java uses these additional files:
 *   SpringUtilities.java
 *   ...
 */

public class AddressBook extends JPanel
                                          implements ActionListener,
                                                     FocusListener {
    Address address;
    // make the List in java.util to distinguish it from the List definition in java.swing
    java.util.List<Address> addresslist = new ArrayList<Address>();
    //The text field controls in the user interface
    //
    JTextField firstnameField,lastnameField,streetField, cityField;
    JFormattedTextField zipField;
    // a spinner control that holds the state names
    JSpinner stateSpinner;
    boolean addressSet = false;
    Font regularFont, italicFont;
    JLabel addressDisplay;
    final static int GAP = 10;

    public AddressBook() {


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
        add(createAddressDisplay());
    }

    protected JComponent createButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

        JButton button = new JButton("Save name and address");

        button.addActionListener(this);
        panel.add(button);

        button = new JButton("Clear address");
        button.addActionListener(this);
        button.setActionCommand("clear");
        panel.add(button);

        //Match the SpringLayout's gap, subtracting 5 to make
        //up for the default gap FlowLayout provides.
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0,
                                                GAP-5, GAP-5));
        return panel;
    }

    /**
     * Called when the user clicks the button or presses
     * Enter in a text field.
     */

    public void actionPerformed(ActionEvent e) {
        if ("clear".equals(e.getActionCommand())) {
            addressSet = false;
            firstnameField.setText("");
            lastnameField.setText("");
            streetField.setText("");
            cityField.setText("");

            //We can't just setText on the formatted text
            //field, since its value will remain set.
            zipField.setValue(null);
        } else {
            
            addAddress();
            printAddresses();
        }
        updateDisplays();
    }

    protected void updateDisplays() {
        addressDisplay.setText(formatAddress());
        if (addressSet) {
            addressDisplay.setFont(regularFont);
        } else {
            addressDisplay.setFont(italicFont);
        }
    }

    protected JComponent createAddressDisplay() {
        JPanel panel = new JPanel(new BorderLayout());
        addressDisplay = new JLabel();
        addressDisplay.setHorizontalAlignment(JLabel.CENTER);
        regularFont = addressDisplay.getFont().deriveFont(Font.PLAIN,
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
        panel.add(addressDisplay,
                  BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(200, 150));

        return panel;
    }
    /**
     * Adds an address to the list and also appends the file with the address,
     * separating values with commas.
     */
    protected void addAddress()
    {
        //Set all the information for the new entry
        address = new Address();
        address.setFirstname(firstnameField.getText());
        address.setLastname(lastnameField.getText());
        address.setStreet(streetField.getText());
        address.setCity(cityField.getText());
        address.setState((String)stateSpinner.getValue());
        address.setZip(zipField.getText());
        addressSet = true;
        addresslist.add(address);
        System.out.println("Address added!!!");
        //Write the entry to the file.
        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("address.txt", true)))) {
         out.println(address.getLastname() + ", " + address.getFirstname() + ", " 
                    + address.getStreet() + ", " + address.getCity() + ", "
                    + address.getState() + ", " + address.getZip());
         out.println("");
        }catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }
    /*
    Prints the list of addresses in the address list. This only prints
    the list being worked on and not the entries from the file.
    */
    protected void printAddresses()
    {
        for(Address a : addresslist)
        {
            System.out.println("first name: " + a.getFirstname());
            System.out.println("last name: " + a.getLastname());
            System.out.println("street: " + a.getStreet());
            System.out.println("city: " + a.getCity());
            System.out.println("State: " + a.getState());
            System.out.println("Zip: " + a.getZip());
        }
    }
    
    protected String formatAddress() {
        if (!addressSet) return "No address set.";
        return address.toString();
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

    /**
     * Called when one of the fields gets the focus so that
     * we can select the focused field.
     */
    public void focusGained(FocusEvent e) {
        Component c = e.getComponent();
        if (c instanceof JFormattedTextField) {
            selectItLater(c);
        } else if (c instanceof JTextField) {
            ((JTextField)c).selectAll();
        }
    }

    //Workaround for formatted text field focus side effects.
    protected void selectItLater(Component c) {
        if (c instanceof JFormattedTextField) {
            final JFormattedTextField ftf = (JFormattedTextField)c;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ftf.selectAll();
                }
            });
        }
    }

    //Needed for FocusListener interface.
    public void focusLost(FocusEvent e) { } //ignore

    protected JComponent createEntryFields() {
        JPanel panel = new JPanel(new SpringLayout());

        String[] labelStrings = {
            "First",
            "Last",
            "Street address: ",
            "City: ",
            "State: ",
            "Zip code: "
        };

        JLabel[] labels = new JLabel[labelStrings.length];
        JComponent[] fields = new JComponent[labelStrings.length];
        int fieldNum = 0;

        //Create the text field and set it up.
        firstnameField  = new JTextField();
        firstnameField.setColumns(20);
        fields[fieldNum++] = firstnameField;

        lastnameField  = new JTextField();
        lastnameField.setColumns(20);
        fields[fieldNum++] = lastnameField;

        streetField  = new JTextField();
        streetField.setColumns(20);
        fields[fieldNum++] = streetField;

        cityField = new JTextField();
        cityField.setColumns(20);
        fields[fieldNum++] = cityField;

        String[] stateStrings = getStateStrings();
        stateSpinner = new JSpinner(new SpinnerListModel(stateStrings));
        fields[fieldNum++] = stateSpinner;

        zipField = new JFormattedTextField(
                            createFormatter("#####"));
        fields[fieldNum++] = zipField;

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

    public String[] getStateStrings() {
        String[] stateStrings = {
            "Alabama (AL)",
            "Alaska (AK)",
            "Arizona (AZ)",
            "Arkansas (AR)",
            "California (CA)",
            "Colorado (CO)",
            "Connecticut (CT)",
            "Delaware (DE)",
            "District of Columbia (DC)",
            "Florida (FL)",
            "Georgia (GA)",
            "Hawaii (HI)",
            "Idaho (ID)",
            "Illinois (IL)",
            "Indiana (IN)",
            "Iowa (IA)",
            "Kansas (KS)",
            "Kentucky (KY)",
            "Louisiana (LA)",
            "Maine (ME)",
            "Maryland (MD)",
            "Massachusetts (MA)",
            "Michigan (MI)",
            "Minnesota (MN)",
            "Mississippi (MS)",
            "Missouri (MO)",
            "Montana (MT)",
            "Nebraska (NE)",
            "Nevada (NV)",
            "New Hampshire (NH)",
            "New Jersey (NJ)",
            "New Mexico (NM)",
            "New York (NY)",
            "North Carolina (NC)",
            "North Dakota (ND)",
            "Ohio (OH)",
            "Oklahoma (OK)",
            "Oregon (OR)",
            "Pennsylvania (PA)",
            "Rhode Island (RI)",
            "South Carolina (SC)",
            "South Dakota (SD)",
            "Tennessee (TN)",
            "Texas (TX)",
            "Utah (UT)",
            "Vermont (VT)",
            "Virginia (VA)",
            "Washington (WA)",
            "West Virginia (WV)",
            "Wisconsin (WI)",
            "Wyoming (WY)"
        };
        return stateStrings;
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

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Address Book");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Add contents to the window.
        frame.add(new AddressBook());

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
