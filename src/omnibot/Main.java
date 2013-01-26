package omnibot;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class creates a GUI for OmniBot
 * @author Pavel Dusek
 */
public class Main extends JFrame implements ActionListener {

    WikiApi api;
    List<WikiPage> allPages;
    List<WikiTalkPage> talkpages;
    List<WikiPage> wikipages;
    List<WikiPage> foundPages;

    private boolean editTalkPages = false;

    private JButton nactiSoubor;
    private JFileChooser fileChooser;

    private JComboBox apiURLCombo;
    private JTextField loginName;
    private JPasswordField password;
    private JButton login;

    private JButton importCriteriaButton;
    private JButton exportCriteriaButton;
    JCheckBox searchWhileParsing;
    JComboBox searchLimitCombo;
    JSpinner searchOffsetSpinner;

    private List<Object> filters;
    private JList filterList;
    private JButton searchButton;

    private JButton pagesSelectAllButton;
    private JButton pagesDeselectAllButton;
    private JScrollPane scrollPane;
    CheckBoxList pageList;
    private JButton exportTitleNamesButton;
    private JButton exportURLLinksButton; //TODO
    private JButton exportResultsButton;
    private JButton exportTalkPageResultsButton;
    private JButton saveDumpButton;
    JTextField summaryTextField;
    JCheckBox minorCheckBox;
    JCheckBox botCheckBox;
    private JButton runButton;
    private JButton runTalkPageButton;

    private JFrame confirmEditFrame;
    private JTextField confirmEditTitleTextField;
    private JScrollPane oldScrollPane;
    private JTextPane oldTextPane;
    private JScrollPane newScrollPane;
    private JTextPane newTextPane;
    private JTextField confirmEditSummaryTextField;
    private JButton confirmEditButton;
    private JButton confirmEditCancelButton;
    private JCheckBox confirmEditMinorCheckBox;
    private JCheckBox confirmEditBotCheckBox;

    private int confirmEditIndex = 0;

    private JFrame exportFrame;
    List<JCheckBox> checkBoxes;

    private WikiPage pageToEdit;


    Main() {
        super("OmniBot");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        SwingUtilities.updateComponentTreeUI(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2));
        JPanel leftPanel = new JPanel(new GridLayout(29, 1));

        //LEFT PANEL
        allPages = new ArrayList<WikiPage>();
        foundPages = new ArrayList<WikiPage>();

        JPanel topPanel = new JPanel(new GridLayout(1,2));
        JLabel nactiSouborLabel = new JLabel("Načti databázový dump:");
        topPanel.add(nactiSouborLabel);
        nactiSoubor = new JButton("…");
        nactiSoubor.addActionListener(this);
        nactiSoubor.setActionCommand("dbDumpOpen");
        topPanel.add(nactiSoubor);
        leftPanel.add(topPanel);                                                 //+row in GridLayout

        leftPanel.add(new JSeparator(JSeparator.HORIZONTAL));                    //+row in GridLayout

        fileChooser = new JFileChooser();

        String[] apis = {
            "http://www.wikiskripta.eu/api.php",
            "http://www.wikilectures.eu/api.php",
            "http://cs.wikipedia.org/w/api.php",
            "http://en.wikipedia.org/w/api.php",
            ""
        };
        apiURLCombo = new JComboBox(apis);
        leftPanel.add(apiURLCombo);                                              //+row in GridLayout
        apiURLCombo.setEditable(true);
        JPanel loginPanel = new JPanel(new GridLayout(1,2));
        JLabel loginNameLabel = new JLabel("Uživatelské jméno:");
        loginName = new JTextField(20);
        loginPanel.add(loginNameLabel);
        loginPanel.add(loginName);
        leftPanel.add(loginPanel);                                               //+row in GridLayout
        JPanel passwordPanel = new JPanel(new GridLayout(1,2));
        JLabel passwordLabel = new JLabel("Heslo:");
        password = new JPasswordField(20);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(password);
        leftPanel.add(passwordPanel);                                            //+row in GridLayout
        login = new JButton("Přihlásit se");
        login.addActionListener(this);
        login.setActionCommand("login");
        leftPanel.add(login);                                                    //+row in GridLayout
        leftPanel.add(new JSeparator(JSeparator.HORIZONTAL));                    //+row in GridLayout

        //SEARCH
        JPanel importCriteriaPanel = new JPanel(new GridLayout(1, 2));
        importCriteriaButton = new JButton("Importovat kritéria");
        importCriteriaButton.addActionListener(this);
        importCriteriaButton.setActionCommand("importCriteria");
        importCriteriaPanel.add(importCriteriaButton);
        exportCriteriaButton = new JButton("Exportovat kritéria");
        exportCriteriaButton.addActionListener(this);
        exportCriteriaButton.setActionCommand("exportCriteria");
        importCriteriaPanel.add(exportCriteriaButton);
        leftPanel.add(importCriteriaPanel);                                      //+row in GridLayout

        searchWhileParsing = new JCheckBox("Hledat již během parsování (pro objemné DB dumpy, např. wikipedie)");
        searchWhileParsing.addActionListener(this);
        searchWhileParsing.setActionCommand("toggleSearchWhileParsing");
        leftPanel.add(searchWhileParsing);                                       //+row in GridLayout
        
        JPanel searchLimitPanel = new JPanel(new GridLayout(1,2));
        JLabel searchLimitLabel = new JLabel("Limit hledaných výsledků:");
        String[] limits = { "10", "100", "200", "500", "1000" };
        searchLimitCombo = new JComboBox(limits);
        searchLimitCombo.setEnabled(false);
        searchLimitPanel.add(searchLimitLabel);
        searchLimitPanel.add(searchLimitCombo);
        leftPanel.add(searchLimitPanel);                                         //+row in GridLayout

        JPanel searchOffsetPanel = new JPanel(new GridLayout(1,2));
        JLabel searchOffsetLabel = new JLabel("Přeskočit nalezených výsledků:");
        SpinnerNumberModel offsetModel = new SpinnerNumberModel(0, 0, 1000, 50);
        searchOffsetSpinner = new JSpinner(offsetModel);
        searchOffsetSpinner.setEnabled(false);
        searchOffsetPanel.add(searchOffsetLabel);
        searchOffsetPanel.add(searchOffsetSpinner);
        leftPanel.add(searchOffsetPanel);                                        //+row in GridLayout

        filters = new ArrayList<Object>();
        filterList = new JList();
        filterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane filterScrollPane = new JScrollPane(filterList);
        leftPanel.add(filterScrollPane);
        JButton addFilter = new JButton("Přidej filtr");
        addFilter.addActionListener(this);
        addFilter.setActionCommand("addFilter");
        leftPanel.add(addFilter);
        JButton removeFilter = new JButton("Odeber filtr");
        removeFilter.addActionListener(this);
        removeFilter.setActionCommand("removeFilter");
        leftPanel.add(removeFilter);

        searchButton = new JButton("Vyhledej");
        searchButton.addActionListener(this);
        searchButton.setActionCommand("search");
        leftPanel.add(searchButton);                                             //+row in GridLayout

        add(leftPanel);

        //RIGHT PANEL
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));

        JPanel pagesSelectionPanel = new JPanel(new GridLayout(1, 2));
        pagesSelectAllButton = new JButton("Označ všechny");
        pagesSelectAllButton.addActionListener(this);
        pagesSelectAllButton.setActionCommand("pagesSelectAll");
        pagesDeselectAllButton = new JButton("Zruš označení");
        pagesDeselectAllButton.addActionListener(this);
        pagesDeselectAllButton.setActionCommand("pagesDeselectAll");
        pagesSelectionPanel.add(pagesSelectAllButton);
        pagesSelectionPanel.add(pagesDeselectAllButton);
        rightPanel.add(pagesSelectionPanel);

        JPanel pagesPanel = new JPanel(new GridLayout(1,1));
        pageList = new CheckBoxList();
        scrollPane = new JScrollPane(pageList);
        pagesPanel.add(scrollPane);
        pagesPanel.setPreferredSize(new Dimension(600, 700));
        rightPanel.add(pagesPanel);

        JPanel saveDumpPanel = new JPanel(new GridLayout(1,2));
        JLabel saveDumpLabel = new JLabel("Ulož tyto stránky do dumpu:");
        saveDumpButton = new JButton("…");
        saveDumpButton.addActionListener(this);
        saveDumpButton.setActionCommand("saveDump");
        saveDumpPanel.add(saveDumpLabel);
        saveDumpPanel.add(saveDumpButton);
        rightPanel.add(saveDumpPanel);

        JPanel exportPanel = new JPanel(new GridLayout(1, 2));
        exportURLLinksButton = new JButton("Seznam URL");
        exportURLLinksButton.addActionListener(this);
        exportURLLinksButton.setActionCommand("exportURL");
        exportPanel.add(exportURLLinksButton);
        exportTitleNamesButton = new JButton("Seznam názvů stránek");
        exportTitleNamesButton.addActionListener(this);
        exportTitleNamesButton.setActionCommand("exportTitleNames");
        exportPanel.add(exportTitleNamesButton);
        rightPanel.add(exportPanel);

        JPanel exportResultsPanel = new JPanel(new GridLayout(1,2));
        exportResultsButton = new JButton("Export výsledků");
        exportResultsButton.addActionListener(this);
        exportResultsButton.setActionCommand("exportResults");
        exportResultsButton.setEnabled(false);
        exportTalkPageResultsButton = new JButton("Export výsledků z diskuzí");
        exportTalkPageResultsButton.addActionListener(this);
        exportTalkPageResultsButton.setActionCommand("exportTalkPageResults");
        exportTalkPageResultsButton.setEnabled(false);
        exportResultsPanel.add(exportResultsButton);
        exportResultsPanel.add(exportTalkPageResultsButton);
        rightPanel.add(exportResultsPanel);



        JPanel bottomPanel = new JPanel(new GridLayout(3,1));
        JPanel summaryPanel = new JPanel(new GridLayout(1,2));
        JLabel summaryLabel = new JLabel("Souhrn editace:");
        summaryTextField = new JTextField(" (via OmniBot)");
        summaryPanel.add(summaryLabel);
        summaryPanel.add(summaryTextField);
        JPanel minorPanel = new JPanel(new GridLayout(1, 2));
        minorCheckBox = new JCheckBox("Malá editace");
        minorCheckBox.setSelected(true);
        minorPanel.add(minorCheckBox);
        botCheckBox = new JCheckBox("Editovat jako bot");
        botCheckBox.setSelected(true);
        bottomPanel.add(minorPanel);
        bottomPanel.add(botCheckBox);
        JPanel runPanel = new JPanel(new GridLayout(1, 2));
        runButton = new JButton("Spustit");
        runButton.addActionListener(this);
        runButton.setActionCommand("run");
        runButton.setSize(searchButton.getSize());
        runTalkPageButton = new JButton("Spustit pro diskusní stránky");
        runTalkPageButton.addActionListener(this);
        runTalkPageButton.setActionCommand("runTalkPage");
        runPanel.add(runButton);
        runPanel.add(runTalkPageButton);
        bottomPanel.add(summaryPanel);
        bottomPanel.add(runPanel);
        rightPanel.add(bottomPanel);
        add(rightPanel);

        setSize(1200, 800);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Main frame = new Main();
        frame.setVisible(true);
    }

    boolean doAllCriteriaApply(WikiPage wikipage) {

        //criteria:
        boolean allCriteriaApply = false;
        boolean titleDoesNotContain, titleRegexDoesNotContain;
        boolean titleContains, titleRegexContains;
        boolean textContains, textDoesNotContain, textRegexContains, textRegexDoesNotContain;
        boolean hasTalkPage, doesNotHaveTalkPage;
        boolean talkPageContains, talkPageDoesNotContain, talkPageRegexContains, talkPageRegexDoesNotContain;

        //TODO criteria from filters
        return false;
        
    }
    
    private void selectAllPages() {
        for (int i = 0; i < pageList.getModel().getSize(); i++) {
            JCheckBox cb = (JCheckBox) pageList.getModel().getElementAt(i);
            cb.setSelected(true);
        }
        pageList.repaint();
    }
    private void deselectAllPages() {
        for (int i = 0; i < pageList.getModel().getSize(); i++) {
            JCheckBox cb = (JCheckBox) pageList.getModel().getElementAt(i);
            cb.setSelected(false);
        }
        pageList.repaint();
    }
    
    void showError(Exception e) {
        JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Chyba!", JOptionPane.ERROR_MESSAGE);
    }
    private void exportCriteria(File f) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element root = doc.createElement("criteria");
            doc.appendChild(root);

            Element el;
            el = doc.createElement("searchWhileParsing");
            if (searchWhileParsing.isSelected()) {
                el.setAttribute("apply", "yes");
                Element searchLimit = doc.createElement("searchLimit");
                String searchLimitValue = (String) searchLimitCombo.getModel().getSelectedItem();
                searchLimit.appendChild(doc.createTextNode(searchLimitValue));
                el.appendChild(searchLimit);
                Element searchOffset = doc.createElement("searchOffset");
                Integer searchOffsetValue = (Integer) searchOffsetSpinner.getModel().getValue();
                searchOffset.appendChild(doc.createTextNode(searchOffsetValue.toString()));
                el.appendChild(searchOffset);
            } else {
                el.setAttribute("apply", "no");
            }
            root.appendChild(el);
            el = doc.createElement("titleString");
            /*
            if (stringTitleSearchCheckBox.isSelected()) {
                el.setAttribute("apply", "yes");
                el.appendChild(doc.createTextNode(stringTitleSearchTextField.getText()));
            } else {
                el.setAttribute("apply", "no");
            }
             * 
             */
            root.appendChild(el);
            el = doc.createElement("summary");
            el.appendChild(doc.createTextNode(summaryTextField.getText()));
            root.appendChild(el);
            el = doc.createElement("minor");
            if (minorCheckBox.isSelected()) {
                el.setAttribute("apply", "yes");
            } else {
                el.setAttribute("apply", "no");
            }
            root.appendChild(el);


            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(f);
            //StreamResult streamResult = new StreamResult(System.out); //for testing only
            transformer.transform(source, streamResult);

            JOptionPane.showMessageDialog(this, "Kritéria uložena!", "OK", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            showError(e);
        }
    }
    private void saveWSDump(File f) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("mediawiki");
            doc.appendChild(rootElement);

            rootElement.setAttribute("xmlns", "http://www.mediawiki.org/xml/export-0.5/");
            rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            rootElement.setAttribute("xsi:schemaLocation", "http://www.mediawiki.org/xml/export-0.5/ http://www.mediawiki.org/xml/export-0.5.xsd");
            rootElement.setAttribute("version", "0.5");
            rootElement.setAttribute("xml:lang", "cs");

            Element siteinfo = doc.createElement("siteinfo");
            rootElement.appendChild(siteinfo);
            Element sitename = doc.createElement("sitename");
            sitename.appendChild(doc.createTextNode("WikiSkripta"));
            siteinfo.appendChild(sitename);
            Element base = doc.createElement("base");
            base.appendChild(doc.createTextNode("http://www.wikiskripta.eu/index.php/Home"));
            siteinfo.appendChild(base);
            Element caseElement = doc.createElement("case");
            caseElement.appendChild(doc.createTextNode("first-letter"));
            siteinfo.appendChild(caseElement);

            Element namespaces = doc.createElement("namespaces");
            Element namespace = doc.createElement("namespace");
            namespace.setAttribute("key", "-2");
            namespace.setAttribute("case", "first-letter");
            namespace.appendChild(doc.createTextNode("Média"));
            namespaces.appendChild(namespace);

            namespace = doc.createElement("namespace");
            namespace.setAttribute("key", "-1");
            namespace.setAttribute("case", "first-letter");
            namespace.appendChild(doc.createTextNode("Speciální"));
            namespaces.appendChild(namespace);

            namespace = doc.createElement("namespace");
            namespace.setAttribute("key", "0");
            namespace.setAttribute("case", "first-letter");
            namespaces.appendChild(namespace);

            namespace = doc.createElement("namespace");
            namespace.setAttribute("key", "1");
            namespace.setAttribute("case", "first-letter");
            namespace.appendChild(doc.createTextNode("Diskuse"));
            namespaces.appendChild(namespace);

            namespace = doc.createElement("namespace");
            namespace.setAttribute("key", "2");
            namespace.setAttribute("case", "first-letter");
            namespace.appendChild(doc.createTextNode("Uživatel"));
            namespaces.appendChild(namespace);

            namespace = doc.createElement("namespace");
            namespace.setAttribute("key", "3");
            namespace.setAttribute("case", "first-letter");
            namespace.appendChild(doc.createTextNode("Diskuse s uživatelem"));
            namespaces.appendChild(namespace);

            namespace = doc.createElement("namespace");
            namespace.setAttribute("key", "4");
            namespace.setAttribute("case", "first-letter");
            namespace.appendChild(doc.createTextNode("WikiSkripta"));
            namespaces.appendChild(namespace);

            namespace = doc.createElement("namespace");
            namespace.setAttribute("key", "5");
            namespace.setAttribute("case", "first-letter");
            namespace.appendChild(doc.createTextNode("Diskuse k WikiSkriptům"));
            namespaces.appendChild(namespace);

            namespace = doc.createElement("namespace");
            namespace.setAttribute("key", "6");
            namespace.setAttribute("case", "first-letter");
            namespace.appendChild(doc.createTextNode("Soubor"));
            namespaces.appendChild(namespace);

            namespace = doc.createElement("namespace");
            namespace.setAttribute("key", "7");
            namespace.setAttribute("case", "first-letter");
            namespace.appendChild(doc.createTextNode("Diskuse k souboru"));
            namespaces.appendChild(namespace);

            namespace = doc.createElement("namespace");
            namespace.setAttribute("key", "8");
            namespace.setAttribute("case", "first-letter");
            namespace.appendChild(doc.createTextNode("MediaWiki"));
            namespaces.appendChild(namespace);

            namespace = doc.createElement("namespace");
            namespace.setAttribute("key", "9");
            namespace.setAttribute("case", "first-letter");
            namespace.appendChild(doc.createTextNode("Diskuse k MediaWiki"));
            namespaces.appendChild(namespace);

            namespace = doc.createElement("namespace");
            namespace.setAttribute("key", "10");
            namespace.setAttribute("case", "first-letter");
            namespace.appendChild(doc.createTextNode("Šablona"));
            namespaces.appendChild(namespace);

            namespace = doc.createElement("namespace");
            namespace.setAttribute("key", "11");
            namespace.setAttribute("case", "first-letter");
            namespace.appendChild(doc.createTextNode("Diskuse k šabloně"));
            namespaces.appendChild(namespace);

            namespace = doc.createElement("namespace");
            namespace.setAttribute("key", "12");
            namespace.setAttribute("case", "first-letter");
            namespace.appendChild(doc.createTextNode("Nápověda"));
            namespaces.appendChild(namespace);

            namespace = doc.createElement("namespace");
            namespace.setAttribute("key", "13");
            namespace.setAttribute("case", "first-letter");
            namespace.appendChild(doc.createTextNode("Diskuse k nápovědě"));
            namespaces.appendChild(namespace);

            namespace = doc.createElement("namespace");
            namespace.setAttribute("key", "14");
            namespace.setAttribute("case", "first-letter");
            namespace.appendChild(doc.createTextNode("Kategorie"));
            namespaces.appendChild(namespace);

            namespace = doc.createElement("namespace");
            namespace.setAttribute("key", "15");
            namespace.setAttribute("case", "first-letter");
            namespace.appendChild(doc.createTextNode("Diskuse ke kategorii"));
            namespaces.appendChild(namespace);

            namespace = doc.createElement("namespace");
            namespace.setAttribute("key", "100");
            namespace.setAttribute("case", "first-letter");
            namespace.appendChild(doc.createTextNode("Portál"));
            namespaces.appendChild(namespace);

            namespace = doc.createElement("namespace");
            namespace.setAttribute("key", "101");
            namespace.setAttribute("case", "first-letter");
            namespace.appendChild(doc.createTextNode("Portál diskuse"));
            namespaces.appendChild(namespace);

            namespace = doc.createElement("namespace");
            namespace.setAttribute("key", "102");
            namespace.setAttribute("case", "first-letter");
            namespace.appendChild(doc.createTextNode("Fórum"));
            namespaces.appendChild(namespace);

            namespace = doc.createElement("namespace");
            namespace.setAttribute("key", "103");
            namespace.setAttribute("case", "first-letter");
            namespace.appendChild(doc.createTextNode("Fórum diskuse"));
            namespaces.appendChild(namespace);
            siteinfo.appendChild(namespaces);

            WikiPage wikipage;
            Element page, title, id, revision, revisionId, revisionTimestamp,
                    revisionContributor, contributorUsername, contributorId,
                    minor, comment, text;
            for (Iterator<WikiPage> it = foundPages.iterator(); it.hasNext(); ) {
                wikipage = it.next();
                if (isPageSelected(wikipage.getTitle())) {
                    page = doc.createElement("page");
                    title = doc.createElement("title");
                    title.appendChild(doc.createTextNode(wikipage.getTitle()));
                    page.appendChild(title);
                    id = doc.createElement("id");
                    id.appendChild(doc.createTextNode(wikipage.getPageId()));
                    page.appendChild(id);
                    revision = doc.createElement("revision");
                    revisionId = doc.createElement("id");
                    revisionId.appendChild(doc.createTextNode(wikipage.getRevisionId()));
                    revision.appendChild(revisionId);
                    revisionTimestamp = doc.createElement("timestamp");
                    revisionTimestamp.appendChild(doc.createTextNode(wikipage.getRevisionTimestamp()));
                    revision.appendChild(revisionTimestamp);
                    revisionContributor = doc.createElement("contributor");
                    contributorUsername = doc.createElement("username");
                    contributorUsername.appendChild(doc.createTextNode(wikipage.getRevisionContributor()));
                    revisionContributor.appendChild(contributorUsername);
                    contributorId = doc.createElement("id");
                    contributorId.appendChild(doc.createTextNode(wikipage.getRevisionContributorId()));
                    revisionContributor.appendChild(contributorId);
                    revision.appendChild(revisionContributor);
                    if (wikipage.isMinor()) {
                        minor = doc.createElement("minor");
                        revision.appendChild(minor);
                    }
                    comment = doc.createElement("comment");
                    comment.appendChild(doc.createTextNode(wikipage.getRevisionComment()));
                    revision.appendChild(comment);
                    text = doc.createElement("text");
                    text.appendChild(doc.createTextNode(wikipage.getPageText()));
                    revision.appendChild(text);
                    page.appendChild(revision);
                    rootElement.appendChild(page);
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(f);
            //StreamResult streamResult = new StreamResult(System.out); //for testing only
            transformer.transform(source, streamResult);

            JOptionPane.showMessageDialog(this, "Dump uložen!", "OK", JOptionPane.INFORMATION_MESSAGE);


        } catch (Exception e) {
            showError(e);;
        }


    }
    private boolean isPageSelected(String pageTitle) {
        JCheckBox checkbox;

        for (int i = 0; i < pageList.getModel().getSize(); i++) {
            checkbox = (JCheckBox) pageList.getModel().getElementAt(i);
            if (checkbox.getText().equals(pageTitle)) {
                return checkbox.isSelected();
            }
        }
        return false;
    }
    private void confirmEdit() {
        pageToEdit = getNextSelectedPage();
        WikiTalkPage talkpage;
        String pageText = "";
        String title = "";
        String timestamp = "";
        /*
        String talkpageNamespace = talkPageNamespaceTextField.getText();
        if (!talkpageNamespace.contains(":")) {
            talkpageNamespace = talkpageNamespace + ":";
        }
         * 
         */

        if (pageToEdit != null) {
            if (editTalkPages) {
                talkpage = pageToEdit.getTalkpage();
                title = talkpage.getTitle();
                pageText = talkpage.getPageText();
            } else {
                title = pageToEdit.getTitle();
                pageText = pageToEdit.getPageText();
            }
            if (api != null) {
                WikiPage apiPage = api.revision(pageToEdit.getTitle());
                if (!apiPage.equalsTo(pageToEdit)) {
                    //TODO
                    JOptionPane.showMessageDialog(this, "Stránka byla od vytvoření databázového dumpu změněna. Ruším editaci.", "Pozor!", JOptionPane.INFORMATION_MESSAGE);
                    confirmEditFrame.setVisible(false);
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Nejste přihlášeni k API! Pracujete offline!", "Pozor!", JOptionPane.ERROR_MESSAGE);
            }

            confirmEditFrame = new JFrame("Potvrďte editaci…");
            confirmEditFrame.setSize(1200, 800);
            confirmEditFrame.setLayout(new BoxLayout(confirmEditFrame.getContentPane(), BoxLayout.PAGE_AXIS));
            JPanel confirmEditTitlePanel = new JPanel(new GridLayout(1, 2));
            JLabel confirmEditTitleLabel = new JLabel("Název:");
            confirmEditTitleTextField = new JTextField(title);
            confirmEditTitleTextField.setEditable(false);
            confirmEditTitlePanel.add(confirmEditTitleLabel);
            confirmEditTitlePanel.add(confirmEditTitleTextField);
            confirmEditFrame.add(confirmEditTitlePanel);

            JPanel timestampPanel = new JPanel(new GridLayout(1, 2));
            JLabel timestampLabel = new JLabel("Timestamp článku:");
            JTextField timestampTextField = new JTextField(pageToEdit.getRevisionTimestamp());
            timestampTextField.setEditable(false);
            timestampPanel.add(timestampLabel);
            timestampPanel.add(timestampTextField);
            confirmEditFrame.add(timestampPanel);

            if (editTalkPages) {
                JPanel talkpageTimestampPanel = new JPanel(new GridLayout(1, 2));
                JLabel talkpageTimestampLabel = new JLabel("Timestamp diskusní stránky:");
                JTextField talkpageTimestampTextField = new JTextField();
                if (pageToEdit.getTalkpage() != null) {
                    talkpageTimestampTextField.setText(pageToEdit.getTalkpage().getRevisionTimestamp());
                } else {
                    talkpageTimestampTextField.setText("-");
                }
                talkpageTimestampTextField.setEditable(false);
                talkpageTimestampPanel.add(talkpageTimestampLabel);
                talkpageTimestampPanel.add(talkpageTimestampTextField);
                confirmEditFrame.add(talkpageTimestampPanel);
            }

            JPanel confirmEditChangesPanel = new JPanel(new GridLayout(1, 1));
            JLabel confirmEditChangesLabel = new JLabel("Změny");
            confirmEditChangesPanel.add(confirmEditChangesLabel);
            confirmEditFrame.add(confirmEditChangesPanel);

            JPanel confirmEditTextPanePanel = new JPanel(new GridLayout(1, 2));
            confirmEditTextPanePanel.setSize(1200, 600);
            oldTextPane = new JTextPane();
            oldTextPane.setEditable(false);


            StyledDocument oldDoc = oldTextPane.getStyledDocument();
            Style regular = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
            Style highlighted = oldDoc.addStyle("highlighted", regular);
            StyleConstants.setBackground(highlighted, Color.yellow);
            StyleConstants.setForeground(highlighted, Color.red);
            try {
                StyledTexts styledTexts = null;
                if (styledTexts != null) {
                    StyledText styledText = styledTexts.getStyledText();
                    while (styledText != null) {
                        oldDoc.insertString(oldDoc.getLength(), styledText.getText(), oldTextPane.getStyle(styledText.getStyleName()));
                        styledText = styledTexts.getStyledText();
                    }
                }
            } catch (Exception e) {
                showError(e);
            }
            newTextPane = new JTextPane();
            newTextPane.setEditable(true);
            StyledDocument newDoc = newTextPane.getStyledDocument();
            try {
                String searchString = "";
                String replacement = "";
                pageText = pageText.replaceAll(searchString, replacement);
                newDoc.insertString(newDoc.getLength(), pageText, StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE));

            } catch (Exception e) {
                showError(e);
            }
            oldScrollPane = new JScrollPane(oldTextPane);
            newScrollPane = new JScrollPane(newTextPane);
            confirmEditTextPanePanel.add(oldScrollPane);
            confirmEditTextPanePanel.add(newScrollPane);
            confirmEditFrame.add(confirmEditTextPanePanel);

            JPanel confirmEditSummaryPanel = new JPanel(new GridLayout(1, 2));
            JLabel confirmEditSummaryLabel = new JLabel("Souhrn editace");
            confirmEditSummaryTextField = new JTextField(summaryTextField.getText());
            confirmEditSummaryPanel.add(confirmEditSummaryLabel);
            confirmEditSummaryPanel.add(confirmEditSummaryTextField);
            confirmEditFrame.add(confirmEditSummaryPanel);

            JPanel confirmEditMinorPanel = new JPanel(new GridLayout(1, 2));
            confirmEditMinorCheckBox = new JCheckBox("Malá editace");
            confirmEditMinorCheckBox.setSelected(minorCheckBox.isSelected());
            confirmEditMinorPanel.add(confirmEditMinorCheckBox);
            confirmEditBotCheckBox = new JCheckBox("Editovat jako bot");
            confirmEditBotCheckBox.setSelected(botCheckBox.isSelected());
            confirmEditMinorPanel.add(confirmEditBotCheckBox);
            confirmEditFrame.add(confirmEditMinorPanel); //TODO checkMinor on main Frame

            JPanel confirmEditButtonPanel = new JPanel(new GridLayout(1, 2));
            confirmEditButton = new JButton("OK");
            confirmEditButton.addActionListener(this);
            confirmEditButton.setActionCommand("editConfirmed");
            confirmEditCancelButton = new JButton("Zrušit");
            confirmEditCancelButton.addActionListener(this);
            confirmEditCancelButton.setActionCommand("editCanceled");
            confirmEditButtonPanel.add(confirmEditButton);
            confirmEditButtonPanel.add(confirmEditCancelButton);
            confirmEditFrame.add(confirmEditButtonPanel);


            confirmEditFrame.setVisible(true);
        } else {
                JOptionPane.showMessageDialog(this, "Není další stránka, která by měla být editována.", "Konec!", JOptionPane.INFORMATION_MESSAGE);
        }
        
    }

    private StyledTexts styledTextsFromStringSearch (String pageText, String searchString) {
                String[] textParts = pageText.split(searchString);
                StyledTexts styledTexts = new StyledTexts();
                if (pageText.startsWith(searchString)) {
                    //the text starts with the searchString, add the styled searchString to the begging
                    styledTexts.addStyledText(searchString, "highlighted");
                }
                //after every textpart added add styled searchString also
                for (int i = 0; i < textParts.length; i++) {
                    styledTexts.addStyledText(textParts[i], "regular");

                    if (i < (textParts.length - 1)) { //otherwise it's the last piece added, no need to add highlighted searchString at the end
                        styledTexts.addStyledText(searchString, "highlighted");
                    }
                }

                if (pageText.endsWith(searchString)) {
                    //the text ends with the searchString, add the styled searchString to the end
                    styledTexts.addStyledText(searchString, "highlighted");
                }
                return styledTexts;
    }
    private StyledTexts StyledTextsFromRegexSearch (String pageText, String regex) {
        String stringPart;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pageText);
        StyledTexts styledTexts = new StyledTexts();
        int index, previousIndex = 0;
        while (matcher.find()) {
            index = matcher.start();
            stringPart = pageText.substring(previousIndex, index);
            styledTexts.addStyledText(stringPart, "regular");
            styledTexts.addStyledText(matcher.group(), "highlighted");
            previousIndex = matcher.end();
        }
        //add text following the last match
        stringPart = pageText.substring(previousIndex);
        styledTexts.addStyledText(stringPart, "regular");
        return styledTexts;
    }
    private void makeEdit() {
        api.editPage(
                pageToEdit,
                newTextPane.getText(),
                confirmEditSummaryTextField.getText(),
                confirmEditMinorCheckBox.isSelected(),
                confirmEditBotCheckBox.isSelected()
        );
        JCheckBox checkBox = (JCheckBox) pageList.getModel().getElementAt(confirmEditIndex);
        checkBox.setSelected(false);
    }
    private WikiPage getPage(String pageTitle) {
        WikiPage page;
        for (Iterator<WikiPage> it = foundPages.iterator(); it.hasNext(); ) {
            page = it.next();
            if (page.getTitle().equals(pageTitle)) {
                return page;
            }
        }
        return null;
    }
    private WikiPage getNextSelectedPage() {
        int max = pageList.getModel().getSize();
        if (confirmEditIndex >= max) {
            //every element of the list has been already returned
            confirmEditIndex = 0;
            return null;
        }

        JCheckBox checkbox = (JCheckBox) pageList.getModel().getElementAt(confirmEditIndex);
        while (!checkbox.isSelected()) {
            confirmEditIndex++;
            if (confirmEditIndex >= max) {
                //every element of the list has been already returned
                confirmEditIndex = 0;
                return null;
            }
            checkbox = (JCheckBox) pageList.getModel().getElementAt(confirmEditIndex);
        }
        return getPage(checkbox.getText());
    }
    public void exportURL() {
        String wsUrls = "";
        String wlUrls = "";
        String twUrls = "";
        String enUrls = "";
        String csUrls = "";
        JCheckBox checkBox;
        for (int i = 0; i < pageList.getModel().getSize(); i++) {
            checkBox = (JCheckBox) pageList.getModel().getElementAt(i);
            if (checkBox.isSelected()) {
                wsUrls += "http://www.wikiskripta.eu/index.php/" + checkBox.getText().replaceAll(" ", "_") + "\n";
                wlUrls += "http://www.wikilectures.eu/index.php/" + checkBox.getText().replaceAll(" ", "_") + "\n";
                twUrls += "http://test-wiki2.lf1.cuni.cz/index.php/" + checkBox.getText().replaceAll(" ", "_") + "\n";
                enUrls += "http://en.wikipedia.org/wiki/" + checkBox.getText().replaceAll(" ", "_") + "\n";
                csUrls += "http://cs.wikipedia.org/wiki/" + checkBox.getText().replaceAll(" ", "_") + "\n";
            }
        }
        String[] texts = {wsUrls, wlUrls, twUrls, enUrls, csUrls};
        showExport(texts);
    }
    public void exportTitleNames() {
        String wikilinks = "";
        String pagenames = "";
        JCheckBox checkBox;
        for (int i = 0; i < pageList.getModel().getSize(); i++) {
            checkBox = (JCheckBox) pageList.getModel().getElementAt(i);
            if (checkBox.isSelected()) {
                if (pagenames.isEmpty()) {
                    pagenames = checkBox.getText();
                } else {
                    pagenames += "|" + checkBox.getText();
                }
                wikilinks += "* [[" + checkBox.getText() + "]]\n";
            }
        }
        String[] texts = {wikilinks, pagenames};
        showExport(texts);
    }
    private void exportResults() {
        String results = "";
        String regexResults = "";
        Set<String> resultsSet = new HashSet<String>();
        JCheckBox checkBox;
        WikiPage page;
        String result;
    }
    private void exportTalkPageResults() {
       String results = "";
        String regexResults = "";
        Set<String> resultsSet = new HashSet<String>();
        JCheckBox checkBox;
        WikiPage page;
        String result;
        //TODO export
        //showExport(texts);
    }
    private void showExport(String[] texts) {
        exportFrame = new JFrame("Export");
        exportFrame.setLayout(new BoxLayout(exportFrame.getContentPane(), BoxLayout.PAGE_AXIS));
        JTextArea area;
        JScrollPane scrollPane;
        for (int i = 0; i < texts.length; i++) {
            area = new JTextArea(texts[i]);
            scrollPane = new JScrollPane(area);
            scrollPane.setMinimumSize(new Dimension(100, 100));
            exportFrame.add(scrollPane);
            exportFrame.add(new JSeparator());
        }
        JButton zavrit = new JButton("Zavřít");
        zavrit.addActionListener(this);
        zavrit.setActionCommand("zavritExport");
        exportFrame.add(zavrit);
        exportFrame.setSize(1200, 800);
        exportFrame.setVisible(true);

    }

    private void sortPages() {
        wikipages = new ArrayList<WikiPage>();
        talkpages = new ArrayList<WikiTalkPage>();

        WikiPage page;
        for (Iterator<WikiPage> it = allPages.iterator(); it.hasNext(); ) {
            page = it.next();
            wikipages.add(page);
        }
    }
    
    /**
     * Method that fires JFileChooser and lets the user choose DB dump and loads it.
     */
    private void dbDumpOpen() {
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (searchWhileParsing.isSelected()) {
                String searchLimitValue = (String) searchLimitCombo.getSelectedItem();
                int searchLimit = Integer.parseInt(searchLimitValue);
                int searchOffset = ((Integer) searchOffsetSpinner.getModel().getValue()).intValue();
                DBDumpSAX dumpParser = new DBDumpSAX(file.getPath(), this, true, searchLimit, searchOffset);
            } else {
                DBDumpSAX dumpParser = new DBDumpSAX(file.getPath(), this);
            }
            this.setCursor(Cursor.getDefaultCursor());
        }    
    }
    /**
     * Method that fires JFileChooser and lets the user choose xml with search
     * criteria and loads it.
     */
    private void importCriteria() {
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            CriteriaParser cp = new CriteriaParser(file.getPath(), this);
            cp.applyCriteria();
        }
    }
    /**
     * Method that fires JFileChooser and lets the user choose xml
     * into which search criteria are to be exported
     */
    private void exportCriteria() {
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            exportCriteria(file);
        }
    }
    /**
     * Method that fires a JFileChooser and lets the user select
     * a xml into which new dump of pages that match the search
     * criteria are exported.
     */
    private void saveDump() {
        String selectedApiUrl = (String) apiURLCombo.getSelectedItem();
        if (selectedApiUrl.equals("http://www.wikiskripta.eu/api.php")) {
            int returnVal = fileChooser.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                saveWSDump(file);
            }
        } else {
            //TODO ukládání dumpu z jiných api
            JOptionPane.showMessageDialog(this, "Ukládání dumpů zatím pouze WikiSkript, jiné projekty nejsou dostupné.", "Chyba!", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Method that uses {@link WikiApi} login method to login to wiki api.
     */
    private void login() {
        api = new WikiApi((String) apiURLCombo.getSelectedItem(), this);
        api.login(loginName.getText(), password.getPassword());
    }
    /**
     * Method that runs the search for pages from the dump matching the criteria.
     * It stores them in the foundPages List.
     */
    private void search() {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        checkBoxes = new ArrayList<JCheckBox>();

        WikiPage page;
        Iterator<WikiPage> it = allPages.iterator();

        for (; it.hasNext();) {
            page = it.next();

            if (doAllCriteriaApply(page)) {
                checkBoxes.add(new JCheckBox(page.getTitle()));
                foundPages.add(page);
            }
        }
        pageList.setListData(checkBoxes.toArray());
        this.setCursor(Cursor.getDefaultCursor());
    }
    /**
     * Method that removes selected filter.
     */
    private void removeFilter() {
        int id = filterList.getSelectedIndex();
        filterList.remove(id);
        filters.remove(id);
    }
    private void addFilter() {
        //TODO
    }
            

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getActionCommand().equals("addFilter")) {
                addFilter();
            }
            if (e.getActionCommand().equals("removeFilter")) {
                removeFilter();
            }
            if (e.getActionCommand().equals("dbDumpOpen")) {
                dbDumpOpen();
            }
            if (e.getActionCommand().equals("importCriteria")) {
                importCriteria();
            }
            if (e.getActionCommand().equals("exportCriteria")) {
                exportCriteria();
            }
            if (e.getActionCommand().equals("saveDump")) {
                saveDump();
            }
            if (e.getActionCommand().equals("exportURL")) {
                exportURL();
            }
            if (e.getActionCommand().equals("exportTitleNames")) {
                exportTitleNames();
            }
            if (e.getActionCommand().equals("pagesSelectAll")) {
                selectAllPages();
            }
            if (e.getActionCommand().equals("pagesDeselectAll")) {
                deselectAllPages();
            }
            if (e.getActionCommand().equals("login")) {
                login();
            }
            if (e.getActionCommand().equals("search")) {
                search();
            }
            if (e.getActionCommand().equals("editConfirmed")) {
                makeEdit();
                confirmEditFrame.setVisible(false);
                confirmEdit();
            }
            if (e.getActionCommand().equals("editCanceled")) {
                confirmEditFrame.setVisible(false);
                JOptionPane.showMessageDialog(this, "Editace byla zrušena.", "Editace zrušena!", JOptionPane.INFORMATION_MESSAGE);
                confirmEdit();
            }
            if (e.getActionCommand().equals("run")) {
                editTalkPages = false;
                confirmEdit();
            }
            if (e.getActionCommand().equals("runTalkPage")) {
                editTalkPages = true;
                confirmEdit();
            }
            if (e.getActionCommand().equals("zavritExport")) {
                exportFrame.setVisible(false);
            }
            if (e.getActionCommand().equals("exportResults")) {
                exportResults();
            }
            if (e.getActionCommand().equals("exportTalkPageResults")) {
                exportTalkPageResults();
            }
        } catch (Exception excp) {
            showError(excp);
            setCursor(Cursor.getDefaultCursor());
        }
    }
}

class StyledTexts {
    List<StyledText> styledTexts;
    int styledTextsIndex = 0;
    StyledTexts() {
        styledTexts = new ArrayList<StyledText>();
    }
    void addStyledText(String text, String styleName) {
        styledTexts.add(new StyledText(text, styleName));
    }
    StyledText getStyledText() {
        if (styledTextsIndex >= styledTexts.size()) {
            return null;
        }
        StyledText st = styledTexts.get(styledTextsIndex);
        styledTextsIndex++;
        return st;
    }

}
class StyledText {
    String text;
    String styleName;

    StyledText (String text, String styleName) {
        this.text = text;
        this.styleName = styleName;
    }
    public String getText() {
        return this.text;
    }
    public String getStyleName() {
        return this.styleName;
    }

}