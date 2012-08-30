package omnibot;


import java.awt.Cursor;
import java.io.InputStream;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pavel
 */
public class DBDumpSAX extends DefaultHandler {
    String value = new String();
    boolean revisionOpen = false;
    boolean contributorOpen = false;
    boolean searchWhileParsing = false;
    int searchLimit;
    int searchOffset;
    int pageIndex = 0;
    WikiPage page;
    Main main;


    DBDumpSAX(String path, Main m) {
        super();
        main = m;
        main.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            parse(path);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(main, e.getLocalizedMessage(), "Chyba!", JOptionPane.ERROR_MESSAGE);
        }
        finally {
            main.setCursor(Cursor.getDefaultCursor());
        }
    }
   DBDumpSAX(String path, Main m, boolean swp, int limit, int offset) {
        super();
        main = m;
        searchWhileParsing = swp;
        searchLimit = limit;
        searchOffset = offset;
        main.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (searchWhileParsing) {
            main.checkBoxes = new ArrayList<JCheckBox>();
        }
        try {
            parse(path);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(main, e.getLocalizedMessage(), "Chyba!", JOptionPane.ERROR_MESSAGE);
        }
        finally {
            main.pageList.setListData(main.checkBoxes.toArray());
            main.setCursor(Cursor.getDefaultCursor());
        }
    }
    @Override
    public void startElement(String uri, String name, String qName, Attributes attributes) {
        if (name.equals("page")) {
            page = new WikiPage();
        }
        if (name.equals("revision")) {
            revisionOpen = true;
        }
        if (name.equals("contributor")) {
            contributorOpen = true;
        }
    }
    @Override
    public void endElement(String uri, String name, String qName) {
        value = value.trim();
        if (name.equals("revision")) {
            revisionOpen = false;
        }
        if (name.equals("contributor")) {
            contributorOpen = false;
        }
        if (name.equals("title")) {
            page.setTitle(value);
        }
        if (name.equals("id")) {
            if (revisionOpen) {
                if (contributorOpen) {
                    page.setRevisionContributorId(value);
                } else {
                    page.setRevisionId(value);
                }
            } else {
                page.setPageId(value);
            }
        }
        if (name.equals("timestamp")) {
            page.setRevisionTimestamp(value);
        }
        if (name.equals("minor")) {
            page.setMinor(true);
        }
        if (name.equals("username")) {
            page.setRevisionContributor(value);
        }
        if (name.equals("comment")) {
            page.setRevisionComment(value);
        }
        if (name.equals("text")) {
            page.setPageText(value);
        }
        if (name.equals("page")) {
            if (searchWhileParsing) {
                if (main.doAllCriteriaApply(page)) {
                    pageIndex++;
                    if ( (pageIndex >= searchOffset) && ((pageIndex - searchOffset) <= searchLimit) ) {
                        main.checkBoxes.add(new JCheckBox(page.getTitle()));
                        main.foundPages.add(page);
                    }
                }
                /* //TODO
                 * if (pageIndex - searchOffset <= searchLimit) {
                 *      end parsing!
                 * }
                 */
            } else {
                    main.allPages.add(page);
            }
        }
        value = new String();
    }
    @Override
    public void characters(char [] ch, int start, int length) {
        value = value + new String(ch, start, length);
    }

    public void parse(String pathToXml)
            throws org.xml.sax.SAXException, java.io.IOException {
        XMLReader xr = XMLReaderFactory.createXMLReader();
        xr.setContentHandler(this);
        xr.setErrorHandler(this);
        xr.parse(pathToXml);
    }
    public void parse(InputStream stream)
            throws org.xml.sax.SAXException, java.io.IOException {
        XMLReader xr = XMLReaderFactory.createXMLReader();
        xr.setContentHandler(this);
        xr.setErrorHandler(this);
        xr.parse(new InputSource(stream));
    }
}
