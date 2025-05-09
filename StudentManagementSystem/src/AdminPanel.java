package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class AdminPanel extends JFrame {
    private StudentDAO studentDAO;
    private JTable studentTable;
    private DefaultTableModel tableModel;

    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField courseField;

    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;

    public AdminPanel() {
        studentDAO = new StudentDAO();

        setTitle("Student Management System - Admin Panel");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Table setup
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Email", "Phone", "Course"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false; // make table non-editable directly
            }
        };
        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        add(scrollPane, BorderLayout.CENTER);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Course:"));
        courseField = new JTextField();
        formPanel.add(courseField);

        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);

        loadStudents();

        // Table row selection listener
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && studentTable.getSelectedRow() != -1) {
                int selectedRow = studentTable.getSelectedRow();
                nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                emailField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                phoneField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                courseField.setText(tableModel.getValueAt(selectedRow, 4).toString());
            }
        });

        // Button actions
        addButton.addActionListener(e -> addStudent());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        clearButton.addActionListener(e -> clearFields());
    }

    private void loadStudents() {
        tableModel.setRowCount(0);
        List<Student> students = studentDAO.getAllStudents();
        for (Student s : students) {
            tableModel.addRow(new Object[]{s.getId(), s.getName(), s.getEmail(), s.getPhone(), s.getCourse()});
        }
    }

    private void addStudent() {
        if (validateFields()) {
            Student student = new Student();
            student.setName(nameField.getText());
            student.setEmail(emailField.getText());
            student.setPhone(phoneField.getText());
            student.setCourse(courseField.getText());

            studentDAO.addStudent(student);
            loadStudents();
            clearFields();
            JOptionPane.showMessageDialog(this, "Student added successfully.");
        }
    }

    private void updateStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to update.");
            return;
        }
        if (validateFields()) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            Student student = new Student();
            student.setId(id);
            student.setName(nameField.getText());
            student.setEmail(emailField.getText());
            student.setPhone(phoneField.getText());
            student.setCourse(courseField.getText());

            studentDAO.updateStudent(student);
            loadStudents();
            clearFields();
            JOptionPane.showMessageDialog(this, "Student updated successfully.");
        }
    }

    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this student?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            studentDAO.deleteStudent(id);
            loadStudents();
            clearFields();
            JOptionPane.showMessageDialog(this, "Student deleted successfully.");
        }
    }

    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        courseField.setText("");
        studentTable.clearSelection();
    }

    private boolean validateFields() {
        if (nameField.getText().isEmpty() || emailField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Email are required fields.");
            return false;
        }
        return true;
    }
}
