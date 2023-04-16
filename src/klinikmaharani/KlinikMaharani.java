package klinikmaharani;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class KlinikMaharani extends JFrame implements ActionListener {

    // Inisiasi variable UI Component
    private JTextField nameField, addressField, nikField, dobField;
    private JButton addButton, updateButton, deleteButton, prevButton, nextButton, listButton, exitButton;
    private JTable table;
    private JLabel nameLabel, addressLabel, nikLabel, dobLabel;
    private DefaultTableModel model;
    private JScrollPane scrollPane;

    private ArrayList<Patient> patients;
    private int currentIndex = -1;

    public KlinikMaharani() {
        // Layouting UI
        setTitle("Klinik Maharani");
        setSize(800, 600);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        nameLabel = new JLabel("Nama Pasien");
        nameLabel.setBounds(50, 50, 100, 30);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(150, 50, 200, 30);
        add(nameField);

        nikLabel = new JLabel("NIK");
        nikLabel.setBounds(50, 100, 100, 30);
        add(nikLabel);

        nikField = new JTextField();
        nikField.setBounds(150, 100, 200, 30);
        add(nikField);

        dobLabel = new JLabel("Tanggal Lahir (YYYY-MM-DD)");
        dobLabel.setBounds(50, 150, 200, 30);
        add(dobLabel);

        dobField = new JTextField();
        dobField.setBounds(250, 150, 100, 30);
        add(dobField);

        addressLabel = new JLabel("Alamat");
        addressLabel.setBounds(50, 200, 100, 30);
        add(addressLabel);

        addressField = new JTextField();
        addressField.setBounds(150, 200, 200, 30);
        add(addressField);

        addButton = new JButton("Tambah");
        addButton.setBounds(50, 250, 100, 30);
        addButton.addActionListener(this);
        add(addButton);

        updateButton = new JButton("Update");
        updateButton.setBounds(160, 250, 100, 30);
        updateButton.addActionListener(this);
        add(updateButton);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(270, 250, 100, 30);
        deleteButton.addActionListener(this);
        add(deleteButton);

        prevButton = new JButton("Sebelumnya");
        prevButton.setBounds(50, 300, 100, 30);
        prevButton.addActionListener(this);
        add(prevButton);

        nextButton = new JButton("Selanjutnya");
        nextButton.setBounds(160, 300, 100, 30);
        nextButton.addActionListener(this);
        add(nextButton);

        listButton = new JButton("List");
        listButton.setBounds(270, 300, 100, 30);
        listButton.addActionListener(this);
        add(listButton);

        exitButton = new JButton("Exit");
        exitButton.setBounds(50, 350, 100, 30);
        exitButton.addActionListener(this);
        add(exitButton);

        model = new DefaultTableModel();
        model.addColumn("Nama Pasien");
        model.addColumn("NIK");
        model.addColumn("Tanggal Lahir");
        model.addColumn("Alamat");
        table = new JTable(model);
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(400, 50, 350, 250);
        add(scrollPane);

        patients = new ArrayList<>();

        showData(0);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addData();
        } else if (e.getSource() == updateButton) {
            updateData();
        } else if (e.getSource() == deleteButton) {
            deleteData();
        } else if (e.getSource() == prevButton) {
            showData(currentIndex - 1);
        } else if (e.getSource() == nextButton) {
            showData(currentIndex + 1);
        } else if (e.getSource() == listButton) {
            listData();
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }

    private void addData() {
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        String nik = nikField.getText().trim();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dob = null;
        try {
            dob = dateFormat.parse(dobField.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Tanggal tidak valid. Masukkan tanggal dengan format angka YYYY-MM-DD.");
            return;
        }

        // Check apakah NIK bersifat unik atau tidak
        for (Patient p : patients) {
            if (p.getNik().equals(nik)) {
                JOptionPane.showMessageDialog(this, "NIK ini sudah terdaftar.");
                return;
            }
        }

        // Add data
        patients.add(new Patient(name, nik, dob, address));

        // Refresh table
        model.addRow(new Object[]{name, nik, dob, address});

        // Reset form
        nameField.setText("");
        addressField.setText("");
        nikField.setText("");
        dobField.setText("");

        // Set the UI to the last data record
        showData(patients.size() - 1);
    }

    private void updateData() {
        if (currentIndex == -1) {
            JOptionPane.showMessageDialog(this, "Masukkan data terlebih dahulu.");
            return;
        }

        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        String nik = nikField.getText().trim();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dob = null;
        try {
            dob = dateFormat.parse(dobField.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Tanggal tidak valid. Masukkan tanggal dengan format angka YYYY-MM-DD.");
            return;
        }

        // Check apakah NIK bersifat unik atau tidak
        for (int i = 0; i < patients.size(); i++) {
            if (i == currentIndex) {
                continue;
            }
            if (patients.get(i).getNik().equals(nik)) {
                JOptionPane.showMessageDialog(this, "NIK ini sudah terdaftar.");
                return;
            }
        }

        // Update data
        Patient p = patients.get(currentIndex);
        p.setName(name);
        p.setAddress(address);
        p.setNik(nik);
        p.setDateOfBirth(dob);

        // Refresh table
        model.setValueAt(name, currentIndex, 0);
        model.setValueAt(address, currentIndex, 1);
        model.setValueAt(nik, currentIndex, 2);
        model.setValueAt(dob, currentIndex, 3);

        // Reset form
        nameField.setText("");
        addressField.setText("");
        nikField.setText("");
        dobField.setText("");

        // Set the UI to the current data record
        showData(currentIndex);
    }

    private void deleteData() {
        if (currentIndex == -1) {
            JOptionPane.showMessageDialog(this, "Masukkan data terlebih dahulu.");
            return;
        }

        // Remove data
        patients.remove(currentIndex);

        // Refresh table
        model.removeRow(currentIndex);

        // Reset form
        nameField.setText("");
        addressField.setText("");
        nikField.setText("");
        dobField.setText("");

        // Set the UI to the first data record
        if (patients.size() > 0) {
            showData(0);
        } else {
            currentIndex = -1;
        }
    }

    private void listData() {
        // Create a new frame
        JFrame frame = new JFrame("Data Pasien");

        // Create a table
        DefaultTableModel listModel = new DefaultTableModel();
        listModel.addColumn("No");
        listModel.addColumn("Nama");
        listModel.addColumn("NIK");
        listModel.addColumn("Tanggal Lahir");
        listModel.addColumn("Alamat");

        for (int i = 0; i < patients.size(); i++) {
            Patient p = patients.get(i);
            listModel.addRow(
                    new Object[]{i + 1, p.getName(), p.getNik(), formatDate(p.getDateOfBirth()), p.getAddress()});
        }

        JTable listTable = new JTable(listModel);
        JScrollPane listScrollPane = new JScrollPane(listTable);
        frame.getContentPane().add(listScrollPane, BorderLayout.CENTER);

        // Display frame
        frame.pack();
        frame.setVisible(true);
    }

    private void showData(int index) {
        if (index < 0 || index >= patients.size()) {
            return;
        }

        Patient p = patients.get(index);
        nameField.setText(p.getName());
        addressField.setText(p.getAddress());
        nikField.setText(p.getNik());
        dobField.setText(formatDate(p.getDateOfBirth()));

        currentIndex = index;
    }

    private String formatDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd");
        return dateFormat.format(date);
    }

    public static void main(String[] args) {
        new KlinikMaharani();
    }
}

class Patient {

    private String name;
    private String address;
    private String nik;
    private Date dateOfBirth;

    public Patient(String name, String nik, Date dateOfBirth, String address) {
        this.name = name;
        this.address = address;
        this.nik = nik;
        this.dateOfBirth = dateOfBirth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}

class UniqueNikConstraintException extends Exception {

    public UniqueNikConstraintException() {
        super("NIK harus unik");
    }
}
