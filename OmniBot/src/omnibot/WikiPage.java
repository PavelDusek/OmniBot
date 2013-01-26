/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omnibot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author pavel
 */
public class WikiPage {

    WikiTalkPage talkPage;
    
    String pageTitle;
    String pageId;
    String revisionId;
    String revisionTimestamp;
    String revisionContributor;
    String revisionContributorId;
    String revisionComment;
    String pageText;
    boolean minor;
    WikiPage() {
        pageTitle = new String();
        pageId = new String();
        revisionId = new String();
        revisionTimestamp = new String();
        revisionContributor = new String();
        revisionContributorId = new String();
        revisionComment = new String();
        minor = false;
    }
    void setTalkpage(WikiTalkPage talkpage) {
        talkPage = talkpage;
    }

    void setTitle(String title) {
        pageTitle = title;
    }
    void setPageId(String id) {
        pageId = id;
    }
    void setRevisionId(String id) {
        revisionId = id;
    }
    void setRevisionTimestamp(String timestamp) {
        revisionTimestamp = timestamp;
    }
    void setRevisionContributor(String contributor) {
        revisionContributor = contributor;
    }
    void setRevisionContributorId(String contributorId) {
        revisionContributorId = contributorId;
    }
    void setRevisionComment(String comment) {
        revisionComment = comment;
    }
    void setPageText(String text) {
        pageText = text;
    }
    void setMinor(boolean m) {
        minor = m;
    }
    String getTitle() {
        return pageTitle;
    }
    String getPageId() {
        return pageId;
    }
    String getRevisionId() {
        return revisionId;
    }
    String getRevisionTimestamp() {
        return revisionTimestamp;
    }
    String getRevisionContributor() {
        return revisionContributor;
    }
    String getRevisionContributorId() {
        return revisionContributorId;
    }
    String getRevisionComment() {
        return revisionComment;
    }
    String getPageText() {
        return pageText;
    }
    boolean isMinor() {
        return minor;
    }
    WikiTalkPage getTalkpage() {
        return talkPage;
    }
    public boolean titleContains(String s) {
        if (pageTitle.indexOf(s) != -1) {
            return true;
        } else {
            return false;
        }
    }
    public boolean titleDoesNotContain(String s) {
        if (pageTitle.indexOf(s) == -1) {
            return true;
        } else {
            return false;
        }
    }
    public boolean titleRegexContains(String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(pageTitle);
        return m.find();
    }
    public boolean titleRegexDoesNotContain(String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(pageTitle);
        if (m.find()) {
            return false;
        } else {
            return true;
        }
    }
    public boolean contains(String s) {
        if (pageText.indexOf(s) != -1 ) {
            return true;
        } else {
            return false;
        }
    }
    public boolean doesNotContain(String s) {
        if (pageText.indexOf(s) == -1) {
            return true;
        } else {
            return false;
        }
    }
    public boolean regexContains(String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(pageText);
        return m.find();
    }
    public boolean regexDoesNotContain(String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(pageText);
        if (m.find()) {
            return false;
        } else {
            return true;
        }
    }
    public boolean hasTalkPage() {
        if (talkPage != null) {
            return true;
        } else {
            return false;
        }
    }
    public boolean talkPageContains(String s) {
        if (this.hasTalkPage()) {
            return talkPage.contains(s);
        } else {
            return false;
        }
    }
    public boolean talkPageDoesNotContain(String s) {
        if (this.hasTalkPage()) {
            return talkPage.doesNotContain(s);
        } else {
            return true;
        }
    }
    public boolean talkPageRegexContain(String regex) {
        if (this.hasTalkPage()) {
            return talkPage.regexContains(regex);
        } else {
            return false;
        }
    }
    public boolean talkPageRegexDoesNotContain(String regex) {
        if (this.hasTalkPage()) {
            return talkPage.regexDoesNotContain(regex);
        } else {
            return true;
        }
    }
    public String getRegexGroup(String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pageText);
        return matcher.group();
    }
    public String getTalkPageRegexGroup(String regex) {
        if (talkPage != null) {
            return talkPage.getRegexGroup(regex);
        } else {
            return null;
        }
    }
    public boolean equalsTo(WikiPage page) {
        if (!page.getTitle().equals(pageTitle)) {
            return false;
        }
        if (!page.getPageId().equals(pageId)) {
            return false;
        }
        if (!page.getPageText().equals(pageText)) {
            return false;
        }
        if (!page.getRevisionTimestamp().equals(revisionTimestamp)) {
            return false;
        }
        if (!page.getRevisionId().equals(revisionId)) {
            return false;
        }
        if (!page.getRevisionComment().equals(revisionComment)) {
            return false;
        }
        if (!page.getRevisionContributor().equals(revisionContributor)) {
            return false;
        }
        if (!page.getRevisionContributorId().equals(revisionContributorId)) {
            return false;
        }
        return true;
    }
}
