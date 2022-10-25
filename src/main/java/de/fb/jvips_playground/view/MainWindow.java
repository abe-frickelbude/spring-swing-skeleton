package de.fb.jvips_playground.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import de.fb.jvips_playground.playground.LayoutTestPanel;
import de.fb.jvips_playground.util.Colors;
import de.fb.jvips_playground.util.RenderUtils;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import org.kordamp.ikonli.octicons.Octicons;
import org.kordamp.ikonli.swing.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.fb.jvips_playground.annotations.SwingView;
import de.fb.jvips_playground.controller.MainWindowController;

@SwingView
public class MainWindow extends JFrame {

    private static final Logger logger = LoggerFactory.getLogger(MainWindow.class);

    private final MainWindowController controller;

    private JComboBox<String> sourceSelectionBox;
    private JButton connectButton;
    private JButton exitButton;
    private JButton stopButton;
    private JButton runButton;

    private JTabbedPane activityTabPane;
    private JPanel controlPanel;
    private SimpleDisplayPanel mainDisplayPanel;
    private HeapMonitor heapMonitorPanel;

    @Inject
    public MainWindow(final MainWindowController controller) {

        super();
        this.controller = controller;
        // default close does nothing to prevent accidentally shutting down application at an inappropriate time
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        initializeUI();
    }

    @PostConstruct
    private void init() {
        connectEventHandlers();
    }

    private void createHeapMonitor() {

        heapMonitorPanel = new HeapMonitor();
        heapMonitorPanel.setPreferredSize(new Dimension(496, 105));
        heapMonitorPanel.setBorder(new CompoundBorder(new EmptyBorder(5, 10, 10, 5),
            new TitledBorder(
                UIManager.getBorder("TitledBorder.border"), "Memory usage monitor",
                TitledBorder.LEADING, TitledBorder.TOP, null)));
        heapMonitorPanel.setEnabled(true);
    }

    private void initializeUI() {

        this.setTitle("JVips Playground");

        // kustom icon for the window title bar!
        setIconImage(RenderUtils.renderFontIcon(FontIcon.of(Octicons.ZAP, DarculaUiColors.WHITE)));

        this.setBounds(100, 100, 776, 765);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel leftPane = new JPanel();
        this.getContentPane().add(leftPane, BorderLayout.CENTER);
        leftPane.setLayout(new BorderLayout(0, 0));

        mainDisplayPanel = new SimpleDisplayPanel();
        mainDisplayPanel.setBorder(new CompoundBorder(new EmptyBorder(5, 10, 10, 5),
            new TitledBorder(
                UIManager.getBorder("TitledBorder.border"), "Output image",
                TitledBorder.LEADING, TitledBorder.TOP, null)));

        mainDisplayPanel.setCrossHairEnabled(true);
        mainDisplayPanel.setCrossHairColor(Color.green);
        mainDisplayPanel.setForegroundColor(Color.green);
        mainDisplayPanel.setBackgroundColor(Colors.TRANSPARENT);
        leftPane.add(mainDisplayPanel, BorderLayout.CENTER);

        createHeapMonitor();
        leftPane.add(heapMonitorPanel, BorderLayout.SOUTH);

//        activityTabPane = new JTabbedPane(SwingConstants.TOP);
//        activityTabPane.setBorder(new EmptyBorder(10, 10, 5, 5));
//        leftPane.add(activityTabPane, BorderLayout.CENTER);

        createControlPanel();
        this.getContentPane().add(controlPanel, BorderLayout.EAST);

        //var testPanel = new LayoutTestPanel();
        //this.getContentPane().add(testPanel.getPanel(), BorderLayout.WEST);
    }

    private void createControlPanel() {

        controlPanel = new JPanel();

        controlPanel.setLayout(new FormLayout(
            new ColumnSpec[]{
                FormSpecs.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("150px:grow"),
                FormSpecs.RELATED_GAP_COLSPEC,
            },
            new RowSpec[]{
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                RowSpec.decode("default:grow"),
                FormSpecs.RELATED_GAP_ROWSPEC,
                RowSpec.decode("default:grow"),
                FormSpecs.RELATED_GAP_ROWSPEC,
                RowSpec.decode("default:grow"),
                RowSpec.decode("43px"),
                FormSpecs.RELATED_GAP_ROWSPEC,
                RowSpec.decode("50px"),
                FormSpecs.RELATED_GAP_ROWSPEC,
                RowSpec.decode("32px"),
                FormSpecs.RELATED_GAP_ROWSPEC,
                RowSpec.decode("32px"),
                FormSpecs.RELATED_GAP_ROWSPEC,
            }));

        controlPanel.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10),
            new EtchedBorder(EtchedBorder.LOWERED, null, null)));

        exitButton = new JButton("Exit");
        exitButton.setIcon(FontIcon.of(Octicons.SIGN_OUT, DarculaUiColors.LIGHT_GRAY));

        connectButton = new JButton("Connect");
        connectButton.setIcon(FontIcon.of(Octicons.RADIO_TOWER, DarculaUiColors.LIGHT_GRAY));

        controlPanel.add(connectButton, "2, 21, fill, fill");
        controlPanel.add(exitButton, "2, 23, fill, fill");
    }

    private void connectEventHandlers() {
        connectButton.addActionListener((event) -> {

        });

//        runButton.addActionListener(event -> {
//
//        });
//
//        stopButton.addActionListener(event -> {
//
//        });

        exitButton.addActionListener(event -> {
            showExitDialog();
        });

//        sourceSelectionBox.addItemListener(event -> {
//            // only send events for the >currently< selected item!
//            if (event.getItem().equals(sourceSelectionBox.getSelectedItem())) {
//
//            }
//        });
    }

    private void showExitDialog() {

        // ask if it is OK to exit
        Boolean exitAllowed = controller.requestAppExit();
        if (exitAllowed) {

            int result = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to exit the application?",
                "Confirm exit", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                controller.exitApp();
            }

        } else {
            JOptionPane.showMessageDialog(null,
                "Cannot exit at this time - please shut down first and wait for all pending tasks to complete!",
                "Exit not possible", JOptionPane.ERROR_MESSAGE);
        }
    }
}
