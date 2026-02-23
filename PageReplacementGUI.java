
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class PageReplacementGUI extends JFrame {
 

    private JTextField framesField, refStringField;
    private JComboBox<String> algoBox;
    private JTextArea resultArea;
    private JTable table;
    private DefaultTableModel model;

    private JProgressBar fifoBar, lruBar, optimalBar;
    private JLabel fifoLabel, lruLabel, optimalLabel;

    public PageReplacementGUI() {

        setTitle("Page Replacement Algorithms - FIFO | LRU | Optimal");
        setSize(900, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(30, 30, 46));

        JLabel title = new JLabel("PAGE REPLACEMENT SIMULATOR", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setBounds(0, 10, 900, 40);
        add(title);

        // ---------------- INPUT PANEL ----------------
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(null);
        inputPanel.setBounds(20, 70, 850, 120);
        inputPanel.setBackground(new Color(45, 45, 60));
        inputPanel.setBorder(new LineBorder(Color.WHITE));
        add(inputPanel);

        JLabel framesLabel = new JLabel("Number of Frames:");
        framesLabel.setForeground(Color.WHITE);
        framesLabel.setBounds(20, 20, 150, 25);
        inputPanel.add(framesLabel);

        framesField = new JTextField();
        framesField.setBounds(160, 20, 120, 25);
        inputPanel.add(framesField);

        JLabel refLabel = new JLabel("Reference String:");
        refLabel.setForeground(Color.WHITE);
        refLabel.setBounds(20, 60, 150, 25);
        inputPanel.add(refLabel);

        refStringField = new JTextField();
        refStringField.setBounds(160, 60, 250, 25);
        inputPanel.add(refStringField);

        JLabel algoLabel = new JLabel("Algorithm:");
        algoLabel.setForeground(Color.WHITE);
        algoLabel.setBounds(450, 20, 150, 25);
        inputPanel.add(algoLabel);

        algoBox = new JComboBox<>(new String[]{"FIFO", "LRU", "Optimal", "Compare All"});
        algoBox.setBounds(530, 20, 150, 25);
        inputPanel.add(algoBox);

        JButton runButton = new JButton("RUN");
        runButton.setBounds(530, 60, 150, 30);
        runButton.setBackground(new Color(0, 180, 90));
        runButton.setForeground(Color.WHITE);
        runButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        inputPanel.add(runButton);

        JButton clearButton = new JButton("CLEAR / RESET");
        clearButton.setBounds(690, 60, 150, 30);
        clearButton.setBackground(new Color(200, 60, 60));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(clearButton);

        // ---------------- TABLE ----------------
        model = new DefaultTableModel();
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(28);

        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(Color.WHITE);

                String status = table.getValueAt(row, 2).toString();
                if (status.equalsIgnoreCase("HIT")) {
                    c.setForeground(new Color(0, 150, 0));
                } else if (status.equalsIgnoreCase("FAULT")) {
                    c.setForeground(Color.RED);
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 210, 850, 250);
        add(scrollPane);

        model.addColumn("Page");
        model.addColumn("Frames");
        model.addColumn("Status");

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        resultArea.setBackground(new Color(45, 45, 60));
        resultArea.setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(resultArea);
        scroll.setBounds(20, 470, 850, 130);
        add(scroll);

        // ---------------- BARS ----------------
        fifoLabel = new JLabel("FIFO Hits: 0");
        fifoLabel.setForeground(Color.WHITE);
        fifoLabel.setBounds(20, 610, 150, 25);
        add(fifoLabel);

        fifoBar = new JProgressBar(0, 100);
        fifoBar.setBounds(170, 610, 200, 25);
        fifoBar.setStringPainted(true);
        add(fifoBar);

        lruLabel = new JLabel("LRU Hits: 0");
        lruLabel.setForeground(Color.WHITE);
        lruLabel.setBounds(20, 640, 150, 25);
        add(lruLabel);

        lruBar = new JProgressBar(0, 100);
        lruBar.setBounds(170, 640, 200, 25);
        lruBar.setStringPainted(true);
        add(lruBar);

        optimalLabel = new JLabel("Optimal Hits: 0");
        optimalLabel.setForeground(Color.WHITE);
        optimalLabel.setBounds(20, 670, 150, 25);
        add(optimalLabel);

        optimalBar = new JProgressBar(0, 100);
        optimalBar.setBounds(170, 670, 200, 25);
        optimalBar.setStringPainted(true);
        add(optimalBar);

        runButton.addActionListener(e -> runAlgorithm());
        clearButton.addActionListener(e -> clearAll());
    }

    private void runAlgorithm() {
        model.setRowCount(0);
        resultArea.setText("");

        try {
            int framesCount = Integer.parseInt(framesField.getText());
            String[] tokens = refStringField.getText().trim().split("\\s+");
            int[] ref = Arrays.stream(tokens).mapToInt(Integer::parseInt).toArray();
            String choice = algoBox.getSelectedItem().toString();

            switch (choice) {
                case "FIFO" -> showResult(fifo(ref, framesCount), "FIFO");
                case "LRU" -> showResult(lru(ref, framesCount), "LRU");
                case "Optimal" -> showResult(optimal(ref, framesCount), "Optimal");
                case "Compare All" -> {
                    Result f = fifo(ref, framesCount);
                    Result l = lru(ref, framesCount);
                    Result o = optimal(ref, framesCount);
                    showComparison(f, l, o);
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid Input! Please enter valid frames and reference string.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ================= RESULT CLASS =================
    static class Result {
        int hits, faults;
        double hitRatio, faultRatio;
        String name;

        Result(String n, int h, int f, int total) {
            name = n;
            hits = h;
            faults = f;
            hitRatio = (h * 100.0) / total;
            faultRatio = (f * 100.0) / total;
        }
    }

    private Result fifo(int[] ref, int framesCount) {
        Queue<Integer> q = new LinkedList<>();
        Set<Integer> frames = new HashSet<>();
        int hits = 0, faults = 0;

        for (int page : ref) {
            if (frames.contains(page)) {
                hits++;
                model.addRow(new Object[]{page, frames.toString(), "HIT"});
            } else {
                faults++;
                if (frames.size() < framesCount) {
                    frames.add(page);
                    q.add(page);
                } else {
                    int removed = q.poll();
                    frames.remove(removed);
                    frames.add(page);
                    q.add(page);
                }
                model.addRow(new Object[]{page, frames.toString(), "FAULT"});
            }
        }
        return new Result("FIFO", hits, faults, ref.length);
    }

    private Result lru(int[] ref, int framesCount) {
        ArrayList<Integer> frames = new ArrayList<>();
        HashMap<Integer, Integer> recent = new HashMap<>();
        int hits = 0, faults = 0;

        for (int i = 0; i < ref.length; i++) {
            int page = ref[i];
            if (frames.contains(page)) {
                hits++;
                recent.put(page, i);
                model.addRow(new Object[]{page, frames.toString(), "HIT"});
            } else {
                faults++;
                if (frames.size() < framesCount) frames.add(page);
                else {
                    int lruPage = frames.get(0);
                    for (int p : frames)
                        if (recent.getOrDefault(p, -1) < recent.getOrDefault(lruPage, -1))
                            lruPage = p;
                    frames.remove(Integer.valueOf(lruPage));
                    frames.add(page);
                }
                recent.put(page, i);
                model.addRow(new Object[]{page, frames.toString(), "FAULT"});
            }
        }
        return new Result("LRU", hits, faults, ref.length);
    }

    private Result optimal(int[] ref, int framesCount) {
        ArrayList<Integer> frames = new ArrayList<>();
        int hits = 0, faults = 0;

        for (int i = 0; i < ref.length; i++) {
            int page = ref[i];
            if (frames.contains(page)) {
                hits++;
                model.addRow(new Object[]{page, frames.toString(), "HIT"});
            } else {
                faults++;
                if (frames.size() < framesCount) frames.add(page);
                else {
                    int farthest = -1, replacePage = frames.get(0);
                    for (int f : frames) {
                        int next = Integer.MAX_VALUE;
                        for (int k = i + 1; k < ref.length; k++)
                            if (ref[k] == f) {
                                next = k;
                                break;
                            }
                        if (next > farthest) {
                            farthest = next;
                            replacePage = f;
                        }
                    }
                    frames.remove(Integer.valueOf(replacePage));
                    frames.add(page);
                }
                model.addRow(new Object[]{page, frames.toString(), "FAULT"});
            }
        }
        return new Result("Optimal", hits, faults, ref.length);
    }

    private void showResult(Result r, String name) {
        resultArea.setText(
                "Algorithm: " + name + "\n" +
                        "Page Hits: " + r.hits + "\n" +
                        "Page Faults: " + r.faults + "\n" +
                        "Hit Ratio: " + Math.round(r.hitRatio) + "%\n" +
                        "Fault Ratio: " + Math.round(r.faultRatio) + "%\n"
        );

        switch (name) {
            case "FIFO" -> {
                fifoLabel.setText("FIFO Hits: " + r.hits);
                fifoBar.setValue((int) Math.round(r.hitRatio));
            }
            case "LRU" -> {
                lruLabel.setText("LRU Hits: " + r.hits);
                lruBar.setValue((int) Math.round(r.hitRatio));
            }
            case "Optimal" -> {
                optimalLabel.setText("Optimal Hits: " + r.hits);
                optimalBar.setValue((int) Math.round(r.hitRatio));
            }
        }
    }

    private void showComparison(Result f, Result l, Result o) {
        resultArea.setText(
                "===== COMPARISON =====\n" +
                        "\nFIFO -> Hits: " + f.hits + " | Faults: " + f.faults +
                        " | Hit%: " + Math.round(f.hitRatio) + "% | Fault%: " + Math.round(f.faultRatio) + "%" +
                        "\nLRU -> Hits: " + l.hits + " | Faults: " + l.faults +
                        " | Hit%: " + Math.round(l.hitRatio) + "% | Fault%: " + Math.round(l.faultRatio) + "%" +
                        "\nOptimal -> Hits: " + o.hits + " | Faults: " + o.faults +
                        " | Hit%: " + Math.round(o.hitRatio) + "% | Fault%: " + Math.round(o.faultRatio) + "%"
        );

        fifoLabel.setText("FIFO Hits: " + f.hits);
        fifoBar.setValue((int) Math.round(f.hitRatio));

        lruLabel.setText("LRU Hits: " + l.hits);
        lruBar.setValue((int) Math.round(l.hitRatio));

        optimalLabel.setText("Optimal Hits: " + o.hits);
        optimalBar.setValue((int) Math.round(o.hitRatio));

        Result best = f;
        if (l.hitRatio > best.hitRatio) best = l;
        if (o.hitRatio > best.hitRatio) best = o;

        resultArea.append("\n\nBEST ALGORITHM: " + best.name.toUpperCase() +
                "\nBecause it has the HIGHEST Hit Ratio and LOWEST Page Faults.");
    }

    private void clearAll() {
        framesField.setText("");
        refStringField.setText("");
        algoBox.setSelectedIndex(0);
        resultArea.setText("");
        model.setRowCount(0);

        fifoLabel.setText("FIFO Hits: 0");
        lruLabel.setText("LRU Hits: 0");
        optimalLabel.setText("Optimal Hits: 0");

        fifoBar.setValue(0);
        lruBar.setValue(0);
        optimalBar.setValue(0);

        JOptionPane.showMessageDialog(this,
                "All fields have been cleared successfully!",
                "Reset Done",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        new PageReplacementGUI().setVisible(true);
    }
}
