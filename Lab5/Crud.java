package Lab5;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;

public class Crud {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private Vector<String> columnNames;
    private Vector<Vector<String>> data;
    private File xmlFile = new File("data.xml");

    public Crud() {
        frame = new JFrame("XML CRUD GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        columnNames = new Vector<>();
        columnNames.add("Tag");
        columnNames.add("Value");
        data = new Vector<>();

        model = new DefaultTableModel(data, columnNames);
        table = new JTable(model);

        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton saveButton = new JButton("Save XML");
        JButton loadButton = new JButton("Load XML");

        JPanel panel = new JPanel();
        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(saveButton);
        panel.add(loadButton);

        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addElement());
        editButton.addActionListener(e -> editElement());
        deleteButton.addActionListener(e -> deleteElement());
        saveButton.addActionListener(e -> saveXML());
        loadButton.addActionListener(e -> loadXML());

        frame.setVisible(true);
    }

    private void addElement() {
        String tag = JOptionPane.showInputDialog("Enter Tag:");
        String value = JOptionPane.showInputDialog("Enter Value:");
        if (tag != null && value != null) {
            Vector<String> row = new Vector<>();
            row.add(tag);
            row.add(value);
            model.addRow(row);
        }
    }

    private void editElement() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String tag = JOptionPane.showInputDialog("Edit Tag:", model.getValueAt(selectedRow, 0));
            String value = JOptionPane.showInputDialog("Edit Value:", model.getValueAt(selectedRow, 1));
            if (tag != null && value != null) {
                model.setValueAt(tag, selectedRow, 0);
                model.setValueAt(value, selectedRow, 1);
            }
        }
    }

    private void deleteElement() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            model.removeRow(selectedRow);
        }
    }

    private void saveXML() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("Data");
            doc.appendChild(root);

            for (int i = 0; i < model.getRowCount(); i++) {
                Element element = doc.createElement(model.getValueAt(i, 0).toString());
                element.appendChild(doc.createTextNode(model.getValueAt(i, 1).toString()));
                root.appendChild(element);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);

            JOptionPane.showMessageDialog(frame, "XML Saved Successfully!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadXML() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();

            model.setRowCount(0);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Vector<String> row = new Vector<>();
                    row.add(node.getNodeName());
                    row.add(node.getTextContent());
                    model.addRow(row);
                }
            }
            JOptionPane.showMessageDialog(frame, "XML Loaded Successfully!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Crud::new);
    }
}
