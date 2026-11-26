import java.util.*;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static int patientCounter = 101;
    static int appointmentCounter = 501;

    // Patient class
    static class Patient {
        String id, name, contact;
        int age;

        Patient(String id, String name, int age, String contact) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.contact = contact;
        }
    }

    // Doctor time slot
    static class Slot {
        String doctor, date, time;
        boolean booked;

        Slot(String doctor, String date, String time) {
            this.doctor = doctor;
            this.date = date;
            this.time = time;
            this.booked = false;
        }
    }

    // Appointment class
    static class Appointment {
        String id, doctor, date, time, status;
        Patient patient;

        Appointment(String id, Patient patient, Slot slot) {
            this.id = id;
            this.patient = patient;
            this.doctor = slot.doctor;
            this.date = slot.date;
            this.time = slot.time;
            this.status = "Confirmed";
        }

        void display() {
            System.out.println("Appointment ID : " + id);
            System.out.println("Patient ID     : " + patient.id);
            System.out.println("Patient Name   : " + patient.name);
            System.out.println("Age            : " + patient.age);
            System.out.println("Contact        : " + patient.contact);
            System.out.println("Doctor         : " + doctor);
            System.out.println("Date           : " + date);
            System.out.println("Time           : " + time);
            System.out.println("Status         : " + status);
        }
    }

    // Queue implementation using linked list
    static class AppointmentQueue {

        static class Node {
            Patient patient;
            String doctor;
            Node next;

            Node(Patient patient, String doctor) {
                this.patient = patient;
                this.doctor = doctor;
            }
        }

        Node front, rear;

        void enqueue(Patient p, String doctor) {
            Node node = new Node(p, doctor);

            if (rear == null) {
                front = rear = node;
            } else {
                rear.next = node;
                rear = node;
            }

            System.out.println("Patient added to waiting queue.");
        }

        Request dequeue() {
            if (front == null)
                return null;

            Request r = new Request(front.patient, front.doctor);

            front = front.next;

            if (front == null)
                rear = null;

            return r;
        }

        void display() {
            if (front == null) {
                System.out.println("Waiting queue is empty.");
                return;
            }

            System.out.println("\nWaiting Queue:");

            Node temp = front;

            while (temp != null) {
                System.out.println(
                    temp.patient.id + " - " +
                    temp.patient.name + " (" +
                    temp.doctor + ")"
                );

                temp = temp.next;
            }
        }
    }

    static class Request {
        Patient patient;
        String doctor;

        Request(Patient p, String d) {
            patient = p;
            doctor = d;
        }
    }

    // Binary Search Tree implementation
    static class AppointmentBST {

        static class Node {
            Appointment appointment;
            int key;
            Node left, right;

            Node(Appointment a) {
                appointment = a;
                key = Integer.parseInt(a.id.substring(1));
            }
        }

        Node root;

        void insert(Appointment a) {
            root = insertNode(root, new Node(a));
        }

        Node insertNode(Node root, Node node) {
            if (root == null)
                return node;

            if (node.key < root.key)
                root.left = insertNode(root.left, node);
            else if (node.key > root.key)
                root.right = insertNode(root.right, node);

            return root;
        }

        Appointment search(String id) {
            if (id == null || !id.matches("A[0-9]+"))
                return null;

            return searchNode(root,
                Integer.parseInt(id.substring(1)));
        }

        Appointment searchNode(Node root, int key) {
            if (root == null)
                return null;

            if (root.key == key)
                return root.appointment;

            if (key < root.key)
                return searchNode(root.left, key);

            return searchNode(root.right, key);
        }

        void displayAll() {
            if (root == null) {
                System.out.println("No appointments found.");
                return;
            }

            inorder(root);
        }

        void inorder(Node root) {
            if (root == null)
                return;

            inorder(root.left);

            System.out.println("\n--------------------");
            root.appointment.display();

            inorder(root.right);
        }

        void searchByName(String name) {
            List<Appointment> result =
                new ArrayList<Appointment>();

            collect(root, name.trim(), result);

            if (result.isEmpty()) {
                System.out.println("Patient not found.");
            } else {
                for (Appointment a : result) {
                    System.out.println("\n--------------------");
                    a.display();
                }
            }
        }

        void collect(Node root, String name,
                     List<Appointment> result) {
            if (root == null)
                return;

            collect(root.left, name, result);

            if (root.appointment.patient.name
                    .equalsIgnoreCase(name)) {
                result.add(root.appointment);
            }

            collect(root.right, name, result);
        }
    }

    static AppointmentQueue queue = new AppointmentQueue();
    static AppointmentBST bst = new AppointmentBST();
    static List<Slot> slots = new ArrayList<Slot>();

    // Initialize available slots
    static void initializeSlots() {
        slots.add(new Slot("Dr. Mehta", "27-11-2026", "09:00 AM"));
        slots.add(new Slot("Dr. Mehta", "27-11-2026", "09:30 AM"));
        slots.add(new Slot("Dr. Mehta", "27-11-2026", "10:00 AM"));

        slots.add(new Slot("Dr. Sharma", "27-11-2026", "09:00 AM"));
        slots.add(new Slot("Dr. Sharma", "27-11-2026", "09:30 AM"));
        slots.add(new Slot("Dr. Sharma", "27-11-2026", "10:00 AM"));
    }

    static String input(String message) {
        while (true) {
            System.out.print(message);
            String s = sc.nextLine().trim();

            if (!s.isEmpty())
                return s;

            System.out.println("Input cannot be empty.");
        }
    }

    static int getNumber(String message, int min, int max) {
        while (true) {
            try {
                System.out.print(message);
                int n = Integer.parseInt(sc.nextLine());

                if (n >= min && n <= max)
                    return n;

            } catch (NumberFormatException e) {
                // Invalid input
            }

            System.out.println("Enter a valid number.");
        }
    }

    static String chooseDoctor() {
        System.out.println("1. Dr. Mehta");
        System.out.println("2. Dr. Sharma");

        int choice = getNumber("Choose Doctor: ", 1, 2);

        return choice == 1 ? "Dr. Mehta" : "Dr. Sharma";
    }

    // Register patient
    static void registerPatient() {
        System.out.println("\n--- Patient Registration ---");

        String name = input("Enter Name: ");
        int age = getNumber("Enter Age: ", 1, 120);

        String contact;

        while (true) {
            contact = input("Enter 10-digit Contact: ");

            if (contact.matches("[0-9]{10}"))
                break;

            System.out.println("Invalid contact number.");
        }

        String doctor = chooseDoctor();

        Patient p = new Patient(
            "P" + patientCounter++, name, age, contact
        );

        System.out.println("Patient ID: " + p.id);

        queue.enqueue(p, doctor);
    }

    // Greedy algorithm for slot allocation
    static void processPatient() {
        Request request = queue.dequeue();

        if (request == null) {
            System.out.println("No patients waiting.");
            return;
        }

        Slot selected = null;

        for (Slot slot : slots) {
            if (!slot.booked &&
                slot.doctor.equalsIgnoreCase(request.doctor)) {
                selected = slot;
                break;
            }
        }

        if (selected == null) {
            System.out.println("No available slot for this doctor.");
            return;
        }

        selected.booked = true;

        Appointment a = new Appointment(
            "A" + appointmentCounter++,
            request.patient,
            selected
        );

        bst.insert(a);

        System.out.println("\nAppointment Confirmed!");
        a.display();
    }

    // Search appointment by ID
    static void searchAppointment() {
        String id = input("Enter Appointment ID: ").toUpperCase();

        Appointment a = bst.search(id);

        if (a == null) {
            System.out.println("Appointment not found.");
        } else {
            System.out.println("\nAppointment Found:");
            a.display();
        }
    }

    // Search patient by name
    static void searchPatient() {
        String name = input("Enter Patient Name: ");
        bst.searchByName(name);
    }

    // Display available slots
    static void displaySlots() {
        System.out.println("\nAvailable Slots:");

        boolean found = false;

        for (Slot s : slots) {
            if (!s.booked) {
                System.out.println(
                    s.doctor + " | " + s.date + " | " + s.time
                );
                found = true;
            }
        }

        if (!found)
            System.out.println("No slots available.");
    }

    // Main menu
    static void menu() {
        System.out.println("\n==================================");
        System.out.println("    DOCTOR APPOINTMENT SCHEDULER");
        System.out.println("==================================");
        System.out.println("1. Register Patient");
        System.out.println("2. Process Next Patient");
        System.out.println("3. Search Appointment by ID");
        System.out.println("4. Search Patient by Name");
        System.out.println("5. Display Waiting Queue");
        System.out.println("6. Display All Appointments");
        System.out.println("7. Display Available Slots");
        System.out.println("8. Exit");
        System.out.println("==================================");
    }

    public static void main(String[] args) {
        initializeSlots();

        System.out.println(
            "Welcome to Doctor Appointment Scheduler!"
        );

        while (true) {
            menu();

            int choice = getNumber("Enter Choice: ", 1, 8);

            switch (choice) {
                case 1:
                    registerPatient();
                    break;

                case 2:
                    processPatient();
                    break;

                case 3:
                    searchAppointment();
                    break;

                case 4:
                    searchPatient();
                    break;

                case 5:
                    queue.display();
                    break;

                case 6:
                    bst.displayAll();
                    break;

                case 7:
                    displaySlots();
                    break;

                case 8:
                    System.out.println("Thank you!");
                    sc.close();
                    return;
            }
        }
    }
}