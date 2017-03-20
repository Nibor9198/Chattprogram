import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class comes from http://www.java2s.com/Code/Java/Swing-Components/ButtonTableExample.htm
 * It is not of my making, I have just made some minor adjustments.
 *
 *
 */




    class JTableButtonRenderer extends IdButton implements TableCellRenderer {

        public JTableButtonRenderer() {
            super();
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            //setText((value == null) ? "" : value.toString());
            String label = (value == null) ? "" : value.toString();
            setText(label.substring(0,label.indexOf(" ")));
            setId(Integer.parseInt(label.substring(label.indexOf(" ")+1)));

            return this;
        }


}

    /**
     * @version 1.0 11/09/98
     */

    class ButtonEditor extends DefaultCellEditor implements ActionListener {
        protected IdButton button;

        private String label;

        private boolean isPushed;

        private ServerList list;

        public ButtonEditor(JCheckBox checkBox, ServerList list) {
            super(checkBox);
            this.list = list;
            button = new IdButton();
            button.setOpaque(true);
            button.addActionListener(list);

            }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            label = (value == null) ? "" : value.toString();
            System.out.println("Label: " + label);
            button.setText(label.substring(0,label.indexOf(" ")));
            button.setId(Integer.parseInt(label.substring(label.indexOf(" ")+1)));
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                //
                //
                JOptionPane.showMessageDialog(button, label + ": Ouch!");
                // System.out.println(label + ": Ouch!");
            }
            isPushed = false;
            return new String(label);
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            list.buttonPress(((IdButton)e.getSource()).getId());
        }
    }

