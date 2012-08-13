/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omnibot;

import javax.swing.JOptionPane;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author pavel
 */
public class CriteriaParser extends DefaultHandler {

    Main main;
    String value = new String();

    boolean titleString = false;
    boolean titleRegex = false;
    boolean titleNegationString = false;
    boolean titleNegationRegex = false;
    boolean textString = false;
    boolean textRegex = false;
    boolean textNegationString = false;
    boolean textNegationRegex = false;
    boolean talkpageNamespace = false;
    boolean talkpageExists = false;
    boolean talkpageDoesNotExist = false;
    boolean talkpageString = false;
    boolean talkpageRegex = false;
    boolean talkpageNegationString = false;
    boolean talkpageNegationRegex = false;
    boolean searchWhileParsing = false;
    boolean textReplace = false;
    boolean talkpageReplace = false;
    boolean talkpageCreate = false;
    boolean minor = false;

    String searchLimitValue = "";
    String searchOffsetValue = "";
    String titleStringValue = "";
    String titleRegexValue = "";
    String titleNegationStringValue = "";
    String titleNegationRegexValue = "";
    String textStringValue = "";
    String textRegexValue = "";
    String textNegationStringValue = "";
    String textNegationRegexValue = "";
    String talkpageNamespaceValue = "";
    String talkpageStringValue = "";
    String talkpageRegexValue = "";
    String talkpageNegationStringValue = "";
    String talkpageNegationRegexValue = "";
    String textReplaceValue = "";
    String talkpageReplaceValue = "";
    String talkpageCreateValue = "";
    String summaryValue = "";

    CriteriaParser (String filePath, Main m) {
        super();
        this.main = m;
        try {
            parse(filePath);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(main, e.getLocalizedMessage(), "Chyba!", JOptionPane.ERROR_MESSAGE);
        }
    }
    @Override
    public void startElement(String uri, String name, String qName, Attributes attributes) {
        if (name.equals("searchWhileParsing")) {
            if (attributes.getValue("apply").equals("yes")) {
                searchWhileParsing = true;
            }
        }
        if (name.equals("titleString")) {
            if (attributes.getValue("apply").equals("yes")) {
                titleString = true;
            }
        }
        if (name.equals("titleNegationString")) {
            if (attributes.getValue("apply").equals("yes")) {
                titleNegationString = true;
            }
        }
        if (name.equals("titleRegex")) {
            if (attributes.getValue("apply").equals("yes")) {
                titleRegex = true;
            }
        }
        if (name.equals("titleNegationRegex")) {
            if (attributes.getValue("apply").equals("yes")) {
                titleNegationRegex = true;
            }
        }
        if (name.equals("textString")) {
            if (attributes.getValue("apply").equals("yes")) {
                textString = true;
            }
        }
        if (name.equals("textNegationString")) {
            if (attributes.getValue("apply").equals("yes")) {
                textNegationString = true;
            }
        }
        if (name.equals("textRegex")) {
            if (attributes.getValue("apply").equals("yes")) {
                textRegex = true;
            }
        }
        if (name.equals("textNegationRegex")) {
            if (attributes.getValue("apply").equals("yes")) {
                textNegationRegex = true;
            }
        }
        if (name.equals("textReplace")) {
            if (attributes.getValue("apply").equals("yes")) {
                textReplace = true;
            }
        }
        if (name.equals("talkpageNamespace")) {
            if (attributes.getValue("apply").equals("yes")) {
                talkpageNamespace = true;
            }
        }
        if (name.equals("talkpageDoesNotExist")) {
            if (attributes.getValue("apply").equals("yes")) {
                talkpageDoesNotExist = true;
            }
        }
        if (name.equals("talkpageExists")) {
            if (attributes.getValue("apply").equals("yes")) {
                talkpageExists = true;
            }
        }
        if (name.equals("talkpageString")) {
            if (attributes.getValue("apply").equals("yes")) {
                talkpageString = true;
            }
        }
        if (name.equals("talkpageNegationString")) {
            if (attributes.getValue("apply").equals("yes")) {
                talkpageNegationString = true;
            }
        }
        if (name.equals("talkpageRegex")) {
            if (attributes.getValue("apply").equals("yes")) {
                talkpageRegex = true;
            }
        }
        if (name.equals("talkpageNegationRegex")) {
            if (attributes.getValue("apply").equals("yes")) {
                talkpageNegationRegex = true;
            }
        }
        if (name.equals("talkpageReplace")) {
            if (attributes.getValue("apply").equals("yes")) {
                talkpageReplace = true;
            }
        }
        if (name.equals("talkpageCreate")) {
            if (attributes.getValue("apply").equals("yes")) {
                talkpageCreate = true;
            }
        }
        if (name.equals("minor")) {
            if (attributes.getValue("apply").equals("yes")) {
                minor = true;
            }
        }
    }
    @Override
    public void endElement(String uri, String name, String qName) {
        if (name.equals("searchLimit")) {
            searchLimitValue = value;
        }
        if (name.equals("searchOffset")) {
            searchOffsetValue = value;
        }
        if (name.equals("titleString")) {
            titleStringValue = value;
        }
        if (name.equals("titleNegationString")) {
            titleNegationStringValue = value;
        }
        if (name.equals("titleRegex")) {
            titleRegexValue = value;
        }
        if (name.equals("titleNegationRegex")) {
            titleNegationRegexValue = value;
        }
        if (name.equals("textString")) {
            textStringValue = value;
        }
        if (name.equals("textNegationString")) {
            textNegationStringValue = value;
        }
        if (name.equals("textRegex")) {
            textRegexValue = value;
        }
        if (name.equals("textNegationRegex")) {
            textNegationRegexValue = value;
        }
        if (name.equals("textReplace")) {
            textReplaceValue = value;
        }
        if (name.equals("talkpageNamespace")) {
            talkpageNamespaceValue = value;
        }
        if (name.equals("talkpageString")) {
            talkpageStringValue = value;
        }
        if (name.equals("talkpageNegationString")) {
            talkpageNegationStringValue = value;
        }
        if (name.equals("talkpageRegex")) {
            talkpageRegexValue = value;
        }
        if (name.equals("talkpageNegationRegex")) {
            talkpageNegationRegexValue = value;
        }
        if (name.equals("talkpageReplace")) {
            talkpageReplaceValue = value;
        }
        if (name.equals("talkpageCreate")) {
            talkpageCreateValue = value;
        }
        if (name.equals("summary")) {
            summaryValue = value;
        }
        value = new String();
    }
    @Override
    public void characters(char[] ch, int start, int length) {
        value += new String(ch, start, length).trim();
    }
    public void parse (String pathToXmlFile)
            throws org.xml.sax.SAXException, java.io.IOException {
        XMLReader xr = XMLReaderFactory.createXMLReader();
        xr.setContentHandler(this);
        xr.setErrorHandler(this);
        xr.parse(pathToXmlFile);
    }
    void applyCriteria() {
        //set everything to initial values
        main.searchWhileParsing.setSelected(false);
        main.searchLimitCombo.setEnabled(false);
        main.searchOffsetSpinner.setEnabled(false);
        main.stringTitleSearchCheckBox.setSelected(false);
        main.stringTitleSearchTextField.setEnabled(false);
        main.stringTitleNegationCheckBox.setSelected(false);
        main.stringTitleNegationTextField.setEnabled(false);
        main.regexTitleSearchCheckBox.setSelected(false);
        main.regexTitleSearchTextField.setEnabled(false);
        main.regexTitleNegationCheckBox.setSelected(false);
        main.regexTitleNegationTextField.setEnabled(false);
        main.stringSearchCheckBox.setSelected(false);
        main.stringSearchTextField.setEnabled(false);
        main.stringNegationCheckBox.setSelected(false);
        main.stringNegationTextField.setEnabled(false);
        main.regexSearchCheckBox.setSelected(false);
        main.regexSearchTextField.setEnabled(false);
        main.regexNegationCheckBox.setSelected(false);
        main.regexNegationTextField.setEnabled(false);
        main.replaceCheckBox.setSelected(false);
        main.replaceCheckBox.setEnabled(false);
        main.replaceTextField.setEnabled(false);
        main.talkPageNamespaceCheckBox.setSelected(false);
        main.talkPageNamespaceTextField.setEnabled(false);
        main.talkPageMissingCheckBox.setSelected(false);
        main.talkPageMissingCheckBox.setEnabled(false);
        main.talkPageCheckBox.setSelected(false);
        main.talkPageCheckBox.setEnabled(false);
        main.talkPageStringSearchCheckBox.setSelected(false);
        main.talkPageStringSearchCheckBox.setEnabled(false);
        main.talkPageStringSearchTextField.setEnabled(false);
        main.talkPageStringNegationCheckBox.setSelected(false);
        main.talkPageStringNegationCheckBox.setEnabled(false);
        main.talkPageStringNegationTextField.setEnabled(false);
        main.talkPageRegexSearchCheckBox.setSelected(false);
        main.talkPageRegexSearchCheckBox.setEnabled(false);
        main.talkPageRegexSearchTextField.setEnabled(false);
        main.talkPageRegexNegationCheckBox.setSelected(false);
        main.talkPageRegexNegationCheckBox.setEnabled(false);
        main.talkPageRegexNegationTextField.setEnabled(false);
        main.talkPageReplaceCheckBox.setSelected(false);
        main.talkPageReplaceCheckBox.setEnabled(false);
        main.talkPageReplaceTextField.setEnabled(false);
        main.talkPageCreateCheckBox.setSelected(false);
        main.talkPageCreateCheckBox.setEnabled(false);
        main.talkPageCreateTextField.setEnabled(false);

        if (searchWhileParsing) {
            main.searchWhileParsing.doClick();
            if (!searchLimitValue.isEmpty()) {
                main.searchLimitCombo.setSelectedItem(searchLimitValue);
            }
            if (!searchOffsetValue.isEmpty()) {
                main.searchOffsetSpinner.setValue(Integer.parseInt(searchOffsetValue));
            }
        }
        if (titleString) {
            main.stringTitleSearchCheckBox.doClick();
            main.stringTitleSearchTextField.setText(titleStringValue);
        }
        if (titleNegationString) {
            main.stringTitleNegationCheckBox.doClick();
            main.stringTitleNegationTextField.setText(titleNegationStringValue);
        }
        if (titleRegex) {
            main.regexTitleSearchCheckBox.doClick();
            main.regexTitleSearchTextField.setText(titleRegexValue);
        }
        if (titleNegationRegex) {
            main.regexTitleNegationCheckBox.doClick();
            main.regexTitleNegationTextField.setText(titleNegationRegexValue);
        }
        if (textString) {
            main.stringSearchCheckBox.doClick();
            main.stringSearchTextField.setText(textStringValue);
        }
        if (textNegationString) {
            main.stringNegationCheckBox.doClick();
            main.stringNegationTextField.setText(textNegationStringValue);
        }
        if (textRegex) {
            main.regexSearchCheckBox.doClick();
            main.regexSearchTextField.setText(textRegexValue);
        }
        if (textNegationRegex) {
            main.regexNegationCheckBox.doClick();
            main.regexNegationTextField.setText(textNegationRegexValue);
        }
        if (textReplace) {
            main.replaceCheckBox.doClick();
            main.replaceTextField.setText(textReplaceValue);
        }
        if (talkpageNamespace) {
            main.talkPageNamespaceCheckBox.doClick();
            main.talkPageNamespaceTextField.setText(talkpageNamespaceValue);
        }
        if (talkpageDoesNotExist) {
            main.talkPageMissingCheckBox.doClick();
        }
        if (talkpageExists) {
            main.talkPageCheckBox.doClick();
        }
        if (talkpageString) {
            main.talkPageStringSearchCheckBox.doClick();
            main.talkPageStringSearchTextField.setText(talkpageStringValue);
        }
        if (talkpageNegationString) {
            main.talkPageStringNegationCheckBox.doClick();
            main.talkPageStringNegationCheckBox.setText(talkpageNegationStringValue);
        }
        if (talkpageRegex) {
            main.talkPageRegexSearchCheckBox.doClick();
            main.talkPageRegexSearchTextField.setText(talkpageRegexValue);
        }
        if (talkpageNegationRegex) {
            main.talkPageRegexNegationCheckBox.doClick();
            main.talkPageRegexNegationTextField.setText(talkpageNegationRegexValue);
        }
        if (talkpageReplace) {
            main.talkPageReplaceCheckBox.doClick();
            main.talkPageReplaceTextField.setText(talkpageReplaceValue);
        }
        if (talkpageCreate) {
            main.talkPageCreateCheckBox.doClick();
            main.talkPageCreateTextField.setText(talkpageCreateValue);
        }
        if (minor) {
            main.minorCheckBox.setSelected(true);
        }
        main.summaryTextField.setText(summaryValue);
    }

}
