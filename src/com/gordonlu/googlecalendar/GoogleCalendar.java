package com.gordonlu.googlecalendar;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.util.AsynchUtil;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.util.CsvUtil;
import com.google.appinventor.components.runtime.util.Dates;
import com.google.appinventor.components.runtime.util.YailList;

import android.graphics.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Integer;
import java.lang.StringBuilder;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;

@DesignerComponent(version = 2,
        description = "An extension that manages your Google Calendars programmatically in App Inventor. Created by Gordon Lu.",
        iconName = "aiwebres/calendar.png",
        category = ComponentCategory.EXTENSION,
        nonVisible = true)
@SimpleObject(external = true)

public class GoogleCalendar extends AndroidNonvisibleComponent {

    String calendarid = "";
    String appsScript = "";

    public GoogleCalendar(ComponentContainer container){
        super(container.$form());
    }

    @DesignerProperty(editorType = "string", defaultValue = "")
    @SimpleProperty(description = "Specifies the URL of your Google Apps Script. This field must not be empty.")
    public void ScriptUrl(String scriptUrl) {
        appsScript = scriptUrl;
    }

    @SimpleProperty(description = "Specifies the URL of your Google Apps Script. This field must not be empty.")
    public String ScriptUrl() {
        return appsScript;
    }

    @DesignerProperty(editorType = "string", defaultValue = "")
    @SimpleProperty(description = "Specifies the calendar ID of the calendar on which the extension will perform actions.")
    public void CalendarId(String i) {
        calendarid = i;
    }

    @SimpleProperty(description = "Specifies the calendar ID of the calendar on which the extension will perform actions.")
    public String CalendarId() {
        return calendarid;
    }

    @SimpleFunction(description = "Attempts to get the timezone of the calendar specified by the CalendarId property,"
            + " and returns the output via the GotCalendarTimeZone event if successful.")
    public void GetCalendarTimezone(){
        AsynchUtil.runAsynchronously(new Runnable() { // Always do get request Asynchronously
            @Override
            public void run() {
                try {
                    final String calendarId = URLEncoder.encode(calendarid, "UTF-8");
                    BufferedReader readStream = new BufferedReader(new InputStreamReader(new URL(appsScript + "?calendarId=" + calendarId + "&action=getTimeZone").openStream()));
                    String readLine; 
                    StringBuilder data = new StringBuilder(); 
                    while ((readLine = readStream.readLine()) != null) data.append(readLine); 
                    readStream.close(); 
                    final String finalData = data.toString(); 
                    form.runOnUiThread(new Runnable() { 
                        @Override
                        public void run() {
                            if (finalData.startsWith("%24")) {
                                Error(finalData.replace("%24", ""));
                            } else {
                                GotCalendarTimeZone(calendarId, finalData);
                            }
                        }
                    });
                } catch (IOException e) {
                    Error(e.getMessage());
                }
            }
        });
    }

    @SimpleFunction(description = "Attempts to get the description of the calendar specified by the CalendarId property,"
            + "and returns the output via the GotCalendarDescription event.")
    public void GetCalendarDescription(){
        AsynchUtil.runAsynchronously(new Runnable() { // Always do get request Asynchronously
            @Override
            public void run() {
                try {
                    final String calendarId = URLEncoder.encode(calendarid, "UTF-8");
                    BufferedReader readStream = new BufferedReader(new InputStreamReader(new URL(appsScript + "?calendarId=" + calendarId + "&action=getDescription").openStream()));
                    String readLine; 
                    StringBuilder data = new StringBuilder(); 
                    while ((readLine = readStream.readLine()) != null) data.append(readLine); 
                    readStream.close(); 
                    final String finalData = data.toString(); 
                    form.runOnUiThread(new Runnable() { 
                        @Override
                        public void run() {
                            if (finalData.startsWith("%24")) {
                                Error(finalData.replace("%24", ""));
                            } else {
                                GotCalendarDescription(calendarId, finalData);
                            }
                        }
                    });
                } catch (IOException e) {
                    Error(e.getMessage());
                }
            }
        });
    }

    @SimpleFunction(description = "Attempts to list all calendars in the account of the owner of the Apps Script,"
            + " and returns the output via the ListedCalendars event.  This can include calendars they do not own"
            + " but shared with them.")
    public void ListAllCalendars(){
        AsynchUtil.runAsynchronously(new Runnable() {
            @Override
            public void run() {
                try {
                    String calendarId = URLEncoder.encode(calendarid, "UTF-8");
                    BufferedReader readStream = new BufferedReader(new InputStreamReader(new URL(appsScript + "?calendarId=" + calendarId + "&action=getAllCalendars").openStream()));
                    String readLine; 
                    StringBuilder data = new StringBuilder(); 
                    while ((readLine = readStream.readLine()) != null) data.append(readLine); 
                    readStream.close(); 
                    final String finalData = data.toString(); 
                    form.runOnUiThread(new Runnable() { 
                        @Override
                        public void run() {
                            if (finalData.startsWith("%24")) {
                                Error(finalData.replace("%24", ""));
                            } else {
                                ListedCalendars(calendarid, YailList.makeList(finalData.split(",")));
                            }
                        }
                    });
                } catch (IOException e) {
                    Error(e.getMessage());
                }
            }
        });
    }

    @SimpleFunction(description = "Unsubscribes from this calendar and invokes the UnsubscribedFromCalendar() event if"
            + " successful. You cannot unsubscribe from a calendar that you own.")
    public void UnsubscribeFromCalendar(){
        AsynchUtil.runAsynchronously(new Runnable() {
            @Override
            public void run() {
                try {
                    String calendarId = URLEncoder.encode(calendarid, "UTF-8");
                    BufferedReader readStream = new BufferedReader(new InputStreamReader(new URL(appsScript + "?calendarId=" + calendarId + "&action=unsubscribe").openStream()));
                    String readLine; 
                    StringBuilder data = new StringBuilder(); 
                    while ((readLine = readStream.readLine()) != null) data.append(readLine); 
                    readStream.close(); 
                    final String finalData = data.toString(); 
                    form.runOnUiThread(new Runnable() { 
                        @Override
                        public void run() {
                            if (finalData.startsWith("%24")) {
                                Error(finalData.replace("%24", ""));
                            } else {
                                UnsubscribedFromCalendar(calendarid);
                            }
                        }
                    });
                } catch (IOException e) {
                    Error(e.getMessage());
                }
            }
        });
    }

    @SimpleFunction(description = "Lists all calendars that are owned by you and returns the output via the" +
            " ListedOwnedCalendars() event.")
    public void ListAllOwnedCalendars(){
        AsynchUtil.runAsynchronously(new Runnable() { // Always do get request Asynchronously
            @Override
            public void run() {
                try {
                    String calendarId = URLEncoder.encode(calendarid, "UTF-8");
                    BufferedReader readStream = new BufferedReader(new InputStreamReader(new URL(appsScript + "?calendarId=" + calendarId + "&action=getAllOwnedCalendars").openStream()));
                    String readLine; 
                    StringBuilder data = new StringBuilder(); 
                    while ((readLine = readStream.readLine()) != null) data.append(readLine); 
                    readStream.close(); 
                    final String finalData = data.toString(); 
                    form.runOnUiThread(new Runnable() { 
                        @Override
                        public void run() {
                            if (finalData.startsWith("%24")) {
                                Error(finalData.replace("%24", ""));
                            } else {
                                ListedOwnedCalendars(calendarid, YailList.makeList(finalData.split(",")));
                            } 
                        }
                    });
                } catch (IOException e) {
                    Error(e.getMessage());
                }
            }
        });
    }

    @SimpleFunction(description = "Lists all creators of the event specified by eventId and returns the output via the"
            + " ListedEventCreators() event.")
    public void ListEventCreators(final String eventId){
        AsynchUtil.runAsynchronously(new Runnable() { // Always do get request Asynchronously
            @Override
            public void run() {
                try {
                    String calendarId = URLEncoder.encode(calendarid, "UTF-8");
                    final String eventid = URLEncoder.encode(eventId, "UTF-8");
                    BufferedReader readStream = new BufferedReader(new InputStreamReader(new URL(appsScript + "?calendarId=" + calendarId + "&action=getCreators&eventId=" + eventid).openStream()));
                    String readLine; 
                    StringBuilder data = new StringBuilder(); 
                    while ((readLine = readStream.readLine()) != null) data.append(readLine); 
                    readStream.close(); 
                    final String finalData = data.toString(); 
                    form.runOnUiThread(new Runnable() { 
                        @Override
                        public void run() {
                            if (finalData.startsWith("%24")) {
                                Error(finalData.replace("%24", ""));
                            } else {
                               ListedEventCreators(calendarid, eventid, YailList.makeList(finalData.split(","))); 
                            } 
                        }
                    });
                } catch (IOException e) {
                    Error(e.getMessage());
                }
            }
        });
    }

    @SimpleFunction(description = "Attempts to get the color of the calendar specified by the CalendarID property," +
            " and returns the output via the GotCalendarColor event.")
    public void GetCalendarColor(){
        AsynchUtil.runAsynchronously(new Runnable() { // Always do get request Asynchronously
            @Override
            public void run() {
                try {
                    String calendarId = URLEncoder.encode(calendarid, "UTF-8");
                    BufferedReader readStream = new BufferedReader(new InputStreamReader(new URL(appsScript + "?calendarId=" + calendarId + "&action=getColor").openStream()));
                    String readLine; 
                    StringBuilder data = new StringBuilder(); 
                    while ((readLine = readStream.readLine()) != null) data.append(readLine); 
                    readStream.close(); 
                    final String finalData = data.toString(); 
                    form.runOnUiThread(new Runnable() { 
                        @Override
                        public void run() {
                            if (finalData.startsWith("%24")) {
                                Error(finalData.replace("%24", ""));
                            } else {
                               GotCalendarColor(calendarid, Color.parseColor(finalData)); 
                            }
                        }
                    });
                } catch (IOException e) {
                    Error(e.getMessage());
                }
            }
        });
    }

    @SimpleFunction(description = "Attempts to set the color of the calendar specified by the CalendarID property," +
            " and invokes the AfterCalendarColorSet property if successful.")
    public void SetCalendarColor(final int color){
        final String hex = ColorToHex(color);
        AsynchUtil.runAsynchronously(new Runnable() { // Always do get request Asynchronously
            @Override
            public void run() {
                try {
                    String calendarId = URLEncoder.encode(calendarid, "UTF-8");
                    BufferedReader readStream = new BufferedReader(new InputStreamReader(new URL(appsScript + "?calendarId=" + calendarId + "&action=setColor&color=" + "%23" + 
                    hex).openStream()));
                    String readLine; 
                    StringBuilder data = new StringBuilder(); 
                    while ((readLine = readStream.readLine()) != null) data.append(readLine); 
                    readStream.close(); 
                    final String finalData = data.toString(); 
                    form.runOnUiThread(new Runnable() { 
                        @Override
                        public void run() {
                            if (finalData.startsWith("%24")) {
                                Error(finalData.replace("%24", ""));
                            } else {
                               AfterCalendarColorSet(calendarid, color); 
                            }
                        }
                    });
                } catch (IOException e) {
                    Error(e.getMessage());
                }
            }
        });
    }

    public String ColorToHex(int color) {
        return Integer.toHexString(color).toUpperCase().substring(2);
    }

    @SimpleFunction(description = "Attempts to set the timezone of the calendar specified by the CalendarId property," +
            " and invokes the AfterCalendarTimezoneSet event if successful.")
    public void SetCalendarTimezone(final String timezone){
        AsynchUtil.runAsynchronously(new Runnable() { // Always do get request Asynchronously
            @Override
            public void run() {
                try {
                    String calendarId = URLEncoder.encode(calendarid, "UTF-8");
                    BufferedReader readStream = new BufferedReader(new InputStreamReader(new URL(appsScript + "?calendarId=" + calendarId + "&action=setTimeZone&newTimeZone=" + 
                    timezone).openStream()));
                    String readLine; 
                    StringBuilder data = new StringBuilder(); 
                    while ((readLine = readStream.readLine()) != null) data.append(readLine); 
                    readStream.close(); 
                    final String finalData = data.toString(); 
                    form.runOnUiThread(new Runnable() { 
                        @Override
                        public void run() {
                            if (finalData.startsWith("%24")) {
                                Error(finalData.replace("%24", ""));
                            } else {
                                AfterCalendarTimezoneSet(calendarid, finalData);
                            }
                        }
                    });
                } catch (IOException e) {
                    Error(e.getMessage());
                }
            }
        });
    }

    @SimpleFunction(description = "Attempts to add a guest to the calendar event specified by the eventId parameter," +
            " and invokes the AddedGuestToEvent event if successful.")
    public void AddGuestToEvent(final String eventId, final String guestEmail){
        AsynchUtil.runAsynchronously(new Runnable() { // Always do get request Asynchronously
            @Override
            public void run() {
                try {
                    String calendarId = URLEncoder.encode(calendarid, "UTF-8");
                    String eventid = URLEncoder.encode(eventId, "UTF-8");
                    BufferedReader readStream = new BufferedReader(new InputStreamReader(new URL(appsScript + "?calendarId=" + calendarId + "&action=addGuest&email=" + 
                    guestEmail + "&eventId=" + eventid).openStream()));
                    String readLine; 
                    StringBuilder data = new StringBuilder(); 
                    while ((readLine = readStream.readLine()) != null) data.append(readLine); 
                    readStream.close(); 
                    final String finalData = data.toString(); 
                    form.runOnUiThread(new Runnable() { 
                        @Override
                        public void run() {
                            if (finalData.startsWith("%24")) {
                                Error(finalData.replace("%24", ""));
                            } else {
                                AddedGuestToEvent(calendarid, eventId, finalData);
                            }
                        }
                    });
                } catch (IOException e) {
                    Error(e.getMessage());
                }
            }
        });
    }

    @SimpleFunction(description = "Attempts to create a new calendar, and invokes the CreatedCalendar event if this" +
            " attempt is successful. The CalendarId property has no effect on this attempt.")
    public void CreateCalendar(final String calendarName){
        AsynchUtil.runAsynchronously(new Runnable() { // Always do get request Asynchronously
            @Override
            public void run() {
                try {
                    String calendarId = URLEncoder.encode(calendarid, "UTF-8");
                    BufferedReader readStream = new BufferedReader(new InputStreamReader(new URL(appsScript + "?calendarId=" + calendarId + "&action=createCalendar&name=" + 
                    calendarName).openStream()));
                    String readLine; 
                    StringBuilder data = new StringBuilder(); 
                    while ((readLine = readStream.readLine()) != null) data.append(readLine); 
                    readStream.close(); 
                    final String finalData = data.toString(); 
                    form.runOnUiThread(new Runnable() { 
                        @Override
                        public void run() {
                            if (finalData.startsWith("%24")) {
                                Error(finalData.replace("%24", ""));
                            } else {
                                CreatedCalendar(finalData, calendarName);
                            }
                        }
                    });
                } catch (IOException e) {
                    Error(e.getMessage());
                }
            }
        });
    }

    @SimpleFunction(description = "Changes the visibility of this calendar in the Google Calendar dashboard, and invokes" +
            " the AfterCalendarSetIfHidden event if successful. This block does not hide the calendar" +
            " from the calendars list.")
    public void SetCalendarHidden(final boolean hideCalendar){
        AsynchUtil.runAsynchronously(new Runnable() { // Always do get request Asynchronously
            @Override
            public void run() {
                try {
                    String calendarId = URLEncoder.encode(calendarid, "UTF-8");
                    BufferedReader readStream = new BufferedReader(new InputStreamReader(new URL(appsScript + "?calendarId=" + calendarId + "&action=setHidden&hide=" + 
                    String.valueOf(hideCalendar)).openStream()));
                    String readLine; 
                    StringBuilder data = new StringBuilder(); 
                    while ((readLine = readStream.readLine()) != null) data.append(readLine); 
                    readStream.close(); 
                    final String finalData = data.toString(); 
                    form.runOnUiThread(new Runnable() { 
                        @Override
                        public void run() {
                            if (finalData.startsWith("%24")) {
                                Error(finalData.replace("%24", ""));
                            } else {
                                boolean hid = false;
                                if (finalData == "true") {
                                    hid = true;
                                } else {
                                    hid = false;
                                }
                                AfterCalendarSetIfHidden(calendarid, hid); 
                            }
                        }
                    });
                } catch (IOException e) {
                    Error(e.getMessage());
                }
            }
        });
    }

    @SimpleFunction(description = "Changes the visibility of this calendar in the Google Calendar dashboard and" +
            " calendars list, and invokes the AfterCalendarSetIfSelected event if successful.")
    public void SetCalendarSelected(final boolean selectCalendar){
        AsynchUtil.runAsynchronously(new Runnable() { // Always do get request Asynchronously
            @Override
            public void run() {
                try {
                    String calendarId = URLEncoder.encode(calendarid, "UTF-8");
                    BufferedReader readStream = new BufferedReader(new InputStreamReader(new URL(appsScript + "?calendarId=" + calendarId + "&action=setSelected&setSelected=" + 
                    String.valueOf(selectCalendar)).openStream()));
                    String readLine; 
                    StringBuilder data = new StringBuilder(); 
                    while ((readLine = readStream.readLine()) != null) data.append(readLine); 
                    readStream.close(); 
                    final String finalData = data.toString(); 
                    form.runOnUiThread(new Runnable() { 
                        @Override
                        public void run() {
                            if (finalData.startsWith("%24")) {
                                Error(finalData.replace("%24", ""));
                            } else {
                                boolean hid = false;
                                if (finalData == "true") {
                                    hid = true;
                                } else {
                                    hid = false;
                                }
                                AfterCalendarSetIfSelected(calendarid, hid);
                            }
                        }
                    });
                } catch (IOException e) {
                    Error(e.getMessage());
                }
            }
        });
    }

    @SimpleEvent(description = "Invoked when the timezone of the calendar is received.")
    public void GotCalendarTimeZone(String calendarId, String timezone){
        EventDispatcher.dispatchEvent(this, "GotCalendarTimeZone", calendarId, timezone);
    }

    @SimpleEvent(description = "Invoked when the color of the calendar is received.")
    public void GotCalendarColor(String calendarId, int color){
        EventDispatcher.dispatchEvent(this, "GotCalendarColor", calendarId, color);
    }

    @SimpleEvent(description = "Invoked after the extension has successfully reset the timezone of the calendar"
            + " with the SetCalendarTimezone() method.")
    public void AfterCalendarTimezoneSet(String calendarId, String newTimezone){
        EventDispatcher.dispatchEvent(this, "AfterCalendarTimezoneSet", calendarId, newTimezone);
    }

    @SimpleEvent(description = "ThInvoked after the extension has successfully added the guest (specified by guestEmail)"
            + " to the calendar event with the AddGuestToEvent() method.")
    public void AddedGuestToEvent(String calendarId, String eventId, String guestEmail){
        EventDispatcher.dispatchEvent(this, "AddedGuestToEvent", calendarId, eventId, guestEmail);
    }

    @SimpleEvent(description = "Invoked after the extension has successfully reset the color of the calendar"
            + " with the SetCalendarColor() method.")
    public void AfterCalendarColorSet(String calendarId, int color) {
        EventDispatcher.dispatchEvent(this, "AfterCalendarColorSet", calendarId, color);
    }

    @SimpleEvent(description = "Invoked after the extension has successfully reset the hidden state of the calendar"
            + " with the SetCalendarHidden() method.")
    public void AfterCalendarSetIfHidden(String calendarId, boolean isHidden) {
        EventDispatcher.dispatchEvent(this, "AfterCalendarSetIfHidden", calendarId, isHidden);
    }

    @SimpleEvent(description = "Invoked after the extension has successfully reset the selected state of the calendar"
            + " with the SetCalendarSelected() event.")
    public void AfterCalendarSetIfSelected(String calendarId, boolean isSelected) {
        EventDispatcher.dispatchEvent(this, "AfterCalendarSetIfSelected", calendarId, isSelected);
    }

    @SimpleEvent(description = "Invoked after the extension has retrieved a list of your calendars with the" +
            " ListCalendars() method.")
    public void ListedCalendars(String calendarId, YailList calendars) {
        EventDispatcher.dispatchEvent(this, "ListedCalendars", calendarId, calendars);
    }

    @SimpleEvent(description = "Invoked after the extension has retrieved a list of event creators with the"
            + " ListEventCreators() method.")
    public void ListedEventCreators(String calendarId, String eventId, YailList creators) {
        EventDispatcher.dispatchEvent(this, "ListedEventCreators", calendarId, eventId, creators);
    }

    @SimpleEvent(description = "Invoked after the extension has retrieved a list of calendars you own.")
    public void ListedOwnedCalendars(String calendarId, YailList calendars){
        EventDispatcher.dispatchEvent(this, "ListedOwnedCalendars", calendarId, calendars);
    }

    @SimpleEvent(description = "Invoked after the extension has successfully reset the description of the calendar"
            + " with the SetCalendarDescription() method.")
    public void AfterCalendarDescriptionSet(String calendarId, String newDescription){
        EventDispatcher.dispatchEvent(this, "AfterCalendarDescriptionSet", calendarId, newDescription);
    }

    @SimpleEvent(description = "Invoked when an error has occurred.")
    public void Error(String message){
        EventDispatcher.dispatchEvent(this, "Error", message);
    }

    @SimpleEvent(description = "This event is fired when a single-whole-day event is created and fires the CreatedSingleWholeDayEvent after this.")
    public void CreatedSingleWholeDayEvent(String title, String eventId){
        EventDispatcher.dispatchEvent(this, "CreatedSingleWholeDayEvent", title, eventId);
    }

    @SimpleEvent(description = "This event is fired when a multiple-whole-days event is created.")
    public void CreatedMultipleWholeDaysEvent(String title, String eventId){
        EventDispatcher.dispatchEvent(this, "CreatedMultipleWholeDaysEvent", title, eventId);
    }

    @SimpleFunction(description = "Creates an event that only covers a single day, and invokes the"
            + " CreatedSingleWholeDayEvent if successful.")
    public void CreateSingleWholeDayEvent(final String title, Calendar date) {
        final String n = Dates.FormatDateTime(date, "MMM d, yyyy");
        AsynchUtil.runAsynchronously(new Runnable() { // Always do get request Asynchronously
            @Override
            public void run() {
                try {
                    String calendarId = URLEncoder.encode(calendarid, "UTF-8");
                    String t = URLEncoder.encode(title, "UTF-8");
                    BufferedReader readStream = new BufferedReader(new InputStreamReader(new URL(appsScript + "?calendarId=" + calendarId + "&action=createSingleWholeDayEvent&title=" + 
                    t + "&date=" + n).openStream()));
                    String readLine; 
                    StringBuilder data = new StringBuilder(); 
                    while ((readLine = readStream.readLine()) != null) data.append(readLine); 
                    readStream.close(); 
                    final String finalData = data.toString(); 
                    form.runOnUiThread(new Runnable() { 
                        @Override
                        public void run() {
                            if (finalData.startsWith("%24")) {
                                Error(finalData.replace("%24", ""));
                            } else {
                               String[] array = finalData.split("%2C");
                            String title = array[0];
                            String eventId = array[1];
                            CreatedSingleWholeDayEvent(title, eventId); 
                            }
                        }
                    });
                } catch (IOException e) {
                    Error(e.getMessage());
                }
            }
        });
    }

    @SimpleFunction(description = "Creates an event that only covers a single day, with custom options. Specify the"
            + " description and location, a list of emails of guests (can be an empty list), and whether Google Calendar"
            + " should send invitation emails to guests.")
    public void CreateSingleWholeDayEventWithOptions(final String title, Calendar date, final String description,
            final String location, final YailList guests, final boolean sendInvites) {
        final String n = Dates.FormatDateTime(date, "MMM d, yyyy");
        final String csv = CsvUtil.toCsvRow(guests).replace("\"", "");
        AsynchUtil.runAsynchronously(new Runnable() { // Always do get request Asynchronously
            @Override
            public void run() {
                try {
                    String calendarId = URLEncoder.encode(calendarid, "UTF-8");
                    String t = URLEncoder.encode(title, "UTF-8");
                    String c = URLEncoder.encode(csv, "UTF-8");
                    String l = URLEncoder.encode(location, "UTF-8");
                    String d = URLEncoder.encode(description, "UTF-8");
                    String b = String.valueOf(sendInvites);
                    BufferedReader readStream = new BufferedReader(new InputStreamReader(new URL(appsScript + "?calendarId=" + calendarId + "&action=createCustomSingleWholeDayEvent&title=" + 
                    t + "&date=" + n + "&description=" + d + "&guests=" + c + "&location=" + l + "&sendInvites=" + b).openStream()));
                    String readLine; 
                    StringBuilder data = new StringBuilder(); 
                    while ((readLine = readStream.readLine()) != null) data.append(readLine); 
                    readStream.close(); 
                    final String finalData = data.toString(); 
                    form.runOnUiThread(new Runnable() { 
                        @Override
                        public void run() {
                            if (finalData.startsWith("%24")) {
                                Error(finalData.replace("%24", ""));
                            } else {
                                String[] array = finalData.split("%2C");
                                String title = array[0];
                                String eventId = array[1];
                                CreatedSingleWholeDayEvent(title, eventId);
                            }
                        }
                    });
                } catch (IOException e) {
                    Error(e.getMessage());
                }
            }
        });
    }

    @SimpleFunction(description = "Creates an event that covers entire days without custom options, with no specific"
            + " start and end time. Specify the description and location, a list of emails of guests"
            + " (can be an empty list), and whether Google Calendar should send invitation emails to guests."
            + " Invokes the CreatedMultipleWholeDaysEvent if successful.")
    public void CreateMultipleWholeDaysEvent(final String title, Calendar startDate, Calendar endDate){
        final String n = Dates.FormatDateTime(startDate, "MMM d, yyyy");
        final String o = Dates.FormatDateTime(endDate, "MMM d, yyyy");
        AsynchUtil.runAsynchronously(new Runnable() { // Always do get request Asynchronously
            @Override
            public void run() {
                try {
                    String calendarId = URLEncoder.encode(calendarid, "UTF-8");
                    String t = URLEncoder.encode(title, "UTF-8");
                    BufferedReader readStream = new BufferedReader(new InputStreamReader(new URL(appsScript + "?calendarId=" + calendarId + "&action=createMultipleWholeDaysEvent&title=" + 
                    t + "&startDate=" + n + "&endDate=" + o).openStream()));
                    String readLine; 
                    StringBuilder data = new StringBuilder(); 
                    while ((readLine = readStream.readLine()) != null) data.append(readLine); 
                    readStream.close(); 
                    final String finalData = data.toString(); 
                    form.runOnUiThread(new Runnable() { 
                        @Override
                        public void run() {
                            String[] array = finalData.split("%2C");
                            String title = array[0];
                            String eventId = array[1];
                            CreatedMultipleWholeDaysEvent(title, eventId);
                        }
                    });
                } catch (IOException e) {
                    Error(e.getMessage());
                }
            }
        });
    }

    @SimpleFunction(description = "Creates an event that covers entire days with options, with no specific start"
            + " and end time. Invokes the CreatedMultipleWholeDaysEvent if successful.")
    public void CreateMultipleWholeDaysEventWithOptions(final String title, Calendar startDate, Calendar endDate, final String description, final String location, final YailList guests, final boolean sendInvites){
        final String csv = CsvUtil.toCsvRow(guests).replace("\"", "");
        final String n = Dates.FormatDateTime(startDate, "MMM d, yyyy");
        final String o = Dates.FormatDateTime(endDate, "MMM d, yyyy");
        AsynchUtil.runAsynchronously(new Runnable() { // Always do get request Asynchronously
            @Override
            public void run() {
                try {
                    String calendarId = URLEncoder.encode(calendarid, "UTF-8");
                    String t = URLEncoder.encode(title, "UTF-8");
                    String c = URLEncoder.encode(csv, "UTF-8");
                    String l = URLEncoder.encode(location, "UTF-8");
                    String d = URLEncoder.encode(description, "UTF-8");
                    String b = String.valueOf(sendInvites);
                    BufferedReader readStream = new BufferedReader(new InputStreamReader(new URL(appsScript + "?calendarId=" + calendarId + "&action=createCustomMultipleWholeDaysEvent&title=" + 
                    t + "&startDate=" + n + "&endDate=" + o + "&description=" + d + "&guests=" + c + "&location=" + l + "&sendInvites=" + b).openStream()));
                    String readLine; 
                    StringBuilder data = new StringBuilder(); 
                    while ((readLine = readStream.readLine()) != null) data.append(readLine); 
                    readStream.close(); 
                    final String finalData = data.toString(); 
                    form.runOnUiThread(new Runnable() { 
                        @Override
                        public void run() {
                            String[] array = finalData.split("%2C");
                            String title = array[0];
                            String eventId = array[1];
                            CreatedMultipleWholeDaysEvent(title, eventId);
                        }
                    });
                } catch (IOException e) {
                    Error(e.getMessage());
                }
            }
        });
    }

    @SimpleFunction(description = "Attempts to retrieve the name of the calendar specified by CalendarId, and"
            + " invokes the GotCalendarName() event if successful.")
    public void GetCalendarName(){
        AsynchUtil.runAsynchronously(new Runnable() { // Always do get request Asynchronously
            @Override
            public void run() {
                try {
                    String calendarId = URLEncoder.encode(calendarid, "UTF-8");
                    BufferedReader readStream = new BufferedReader(new InputStreamReader(new URL(appsScript + "?calendarId=" + calendarId + "&action=getName").openStream()));
                    String readLine; 
                    StringBuilder data = new StringBuilder(); 
                    while ((readLine = readStream.readLine()) != null) data.append(readLine); 
                    readStream.close(); 
                    final String finalData = data.toString(); 
                    form.runOnUiThread(new Runnable() { 
                        @Override
                        public void run() {
                            GotCalendarName(calendarid, finalData);
                        }
                    });
                } catch (IOException e) {
                    Error(e.getMessage());
                }
            }
        });
    }

    @SimpleFunction(description = "Attempts to get the default calendar's calendar ID and fires the GotName event after this.")
    public void GetDefaultCalendarId(){
        AsynchUtil.runAsynchronously(new Runnable() { // Always do get request Asynchronously
            @Override
            public void run() {
                try {
                    String calendarId = URLEncoder.encode(calendarid, "UTF-8");
                    BufferedReader readStream = new BufferedReader(new InputStreamReader(new URL(appsScript + "?calendarId=" + calendarId + "&action=getDefaultCalendarId").openStream()));
                    String readLine; 
                    StringBuilder data = new StringBuilder(); 
                    while ((readLine = readStream.readLine()) != null) data.append(readLine); 
                    readStream.close(); 
                    final String finalData = data.toString(); 
                    form.runOnUiThread(new Runnable() { 
                        @Override
                        public void run() {
                            GotDefaultCalendarId(finalData);
                        }
                    });
                } catch (IOException e) {
                    Error(e.getMessage());
                }
            }
        });
    }

    @SimpleFunction(description = "Attempts to reset the description of the calendar and fires the GotName event after this.")
    public void SetCalendarDescription(final String description){
        AsynchUtil.runAsynchronously(new Runnable() { // Always do get request Asynchronously
            @Override
            public void run() {
                try {
                    String calendarId = URLEncoder.encode(calendarid, "UTF-8");
                    String d = URLEncoder.encode(description, "UTF-8");
                    BufferedReader readStream = new BufferedReader(new InputStreamReader(new URL(appsScript + "?calendarId=" + calendarId + "&action=setDescription&description=" + d).openStream()));
                    String readLine; 
                    StringBuilder data = new StringBuilder(); 
                    while ((readLine = readStream.readLine()) != null) data.append(readLine); 
                    readStream.close(); 
                    final String finalData = data.toString(); 
                    form.runOnUiThread(new Runnable() { 
                        @Override
                        public void run() {
                            AfterCalendarDescriptionSet(calendarid, finalData);
                        }
                    });
                } catch (IOException e) {
                    Error(e.getMessage());
                }
            }
        });
    }

    @SimpleFunction(description = "Attempts to delete the calendar specified by the CalendarId property and invokes"
            + " the DeletedCalendar event if successful.")
    public void DeleteCalendar(){
        AsynchUtil.runAsynchronously(new Runnable() { // Always do get request Asynchronously
            @Override
            public void run() {
                try {
                    String calendarId = URLEncoder.encode(calendarid, "UTF-8");
                    BufferedReader readStream = new BufferedReader(new InputStreamReader(new URL(appsScript + "?calendarId=" + calendarId + "&action=deleteCalendar").openStream()));
                    String readLine; 
                    StringBuilder data = new StringBuilder(); 
                    while ((readLine = readStream.readLine()) != null) data.append(readLine); 
                    readStream.close(); 
                    final String finalData = data.toString(); 
                    form.runOnUiThread(new Runnable() { 
                        @Override
                        public void run() {
                            DeletedCalendar(finalData);
                        }
                    });
                } catch (IOException e) {
                    Error(e.getMessage());
                }
            }
        });
    }

    @SimpleFunction(description = "Attempts to delete the calendar specified by the CalendarId property and invokes"
            + " the DeletedCalendar event if successful.")
    public void DeleteEvent(final String eventId){
        AsynchUtil.runAsynchronously(new Runnable() { // Always do get request Asynchronously
            @Override
            public void run() {
                try {
                    String calendarId = URLEncoder.encode(calendarid, "UTF-8");
                    String e = URLEncoder.encode(eventId, "UTF-8");
                    BufferedReader readStream = new BufferedReader(new InputStreamReader(new URL(appsScript + "?calendarId=" + calendarId + "&action=deleteEvent&eventId=" + e).openStream()));
                    String readLine; 
                    StringBuilder data = new StringBuilder(); 
                    while ((readLine = readStream.readLine()) != null) data.append(readLine); 
                    readStream.close(); 
                    final String finalData = data.toString(); 
                    form.runOnUiThread(new Runnable() { 
                        @Override
                        public void run() {
                            DeletedEvent(calendarid, finalData);
                        }
                    });
                } catch (IOException e) {
                    Error(e.getMessage());
                }
            }
        });
    }

    @SimpleEvent(description = "Invoked when the extension has successfully created a new calendar with the"
            + " CreateCalendar() method.")
    public void CreatedCalendar(String calendarId, String calendarName) {
        EventDispatcher.dispatchEvent(this, "CreatedCalendar", calendarId, calendarName);
    }

    @SimpleEvent(description = "Invoked when the extension has successfully retrieved the name of the calendar"
            + " with the GetCalendarName() method.")
    public void GotCalendarName(String calendarId, String name) {
        EventDispatcher.dispatchEvent(this, "GotCalendarName", calendarId, name);
    }

    @SimpleEvent(description = "Invoked when the extension has successfully retrieved the default calendar's"
            + " calendar ID with the GetDefaultCalendarId() method.")
    public void GotDefaultCalendarId(String id) {
        EventDispatcher.dispatchEvent(this, "GotDefaultCalendarId", id);
    }

    @SimpleEvent(description = "Invoked when the extension has retrieved the description of the calendar"
            + " with the GetCalendarDescription() event.")
    public void GotCalendarDescription(String calendarId, String description) {
        EventDispatcher.dispatchEvent(this, "GotCalendarDescription", calendarId, description);
    }

    @SimpleFunction(description = "Attempts to check if the calendar is hidden in your Calendar app and fires the CheckedIfCalendarIsHidden event after this.")
    public void IsCalendarHidden(){
        AsynchUtil.runAsynchronously(new Runnable() { // Always do get request Asynchronously
            @Override
            public void run() {
                try {
                    String calendarId = URLEncoder.encode(calendarid, "UTF-8");
                    BufferedReader readStream = new BufferedReader(new InputStreamReader(new URL(appsScript + "?calendarId=" + calendarId + "&action=isHidden").openStream()));
                    String readLine; 
                    StringBuilder data = new StringBuilder(); 
                    while ((readLine = readStream.readLine()) != null) data.append(readLine); 
                    readStream.close(); 
                    final String finalData = data.toString(); 
                    form.runOnUiThread(new Runnable() { 
                        @Override
                        public void run() {
                            if (finalData == "true") {
                                CheckedIfCalendarIsHidden(calendarid, true);
                            } else {
                                CheckedIfCalendarIsHidden(calendarid, false);
                            }
                        }
                    });
                } catch (IOException e) {
                    Error(e.getMessage());
                }
            }
        });
    }

    @SimpleFunction(description = "Attempts to check if the calendar is selected, i.e., if the events of this calendar are displayed" + 
    " in your Calendar app and fires the CheckedIfCalendarIsHidden event after this.")
    public void IsCalendarSelected(){
        AsynchUtil.runAsynchronously(new Runnable() { // Always do get request Asynchronously
            @Override
            public void run() {
                try {
                    String calendarId = URLEncoder.encode(calendarid, "UTF-8");
                    BufferedReader readStream = new BufferedReader(new InputStreamReader(new URL(appsScript + "?calendarId=" + calendarId + "&action=isSelected").openStream()));
                    String readLine; 
                    StringBuilder data = new StringBuilder(); 
                    while ((readLine = readStream.readLine()) != null) data.append(readLine); 
                    readStream.close(); 
                    final String finalData = data.toString(); 
                    form.runOnUiThread(new Runnable() { 
                        @Override
                        public void run() {
                            if (finalData == "true") {
                                CheckedIfCalendarIsSelected(calendarid, true);
                            } else {
                                CheckedIfCalendarIsSelected(calendarid, false);
                            }
                        }
                    });
                } catch (IOException e) {
                    Error(e.getMessage());
                }
            }
        });
    }

    @SimpleFunction(description = "Attempts to check if the calendar is your primary calendar and fires the CheckedIfCalendarIsPrimary after this.")
    public void IsMyPrimaryCalendar(){
        AsynchUtil.runAsynchronously(new Runnable() { // Always do get request Asynchronously
            @Override
            public void run() {
                try {
                    String calendarId = URLEncoder.encode(calendarid, "UTF-8");
                    BufferedReader readStream = new BufferedReader(new InputStreamReader(new URL(appsScript + "?calendarId=" + calendarId + "&action=isMyPrimaryCalendar").openStream()));
                    String readLine; 
                    StringBuilder data = new StringBuilder(); 
                    while ((readLine = readStream.readLine()) != null) data.append(readLine); 
                    readStream.close(); 
                    final String finalData = data.toString(); 
                    form.runOnUiThread(new Runnable() { 
                        @Override
                        public void run() {
                            if (finalData == "true") {
                                CheckedIfCalendarIsPrimary(calendarid, true);
                            } else {
                                CheckedIfCalendarIsPrimary(calendarid, false);
                            }
                        }
                    });
                } catch (IOException e) {
                    Error(e.getMessage());
                }
            }
        });
    }

    @SimpleFunction(description = "Attempts to check if the calendar specified by CalendarId is owned by the owner of"
            + " the Apps Script, and invokes the CheckIfCalendarIsYours() event if successful.")
    public void IsCalendarOwnedByYou(){
        AsynchUtil.runAsynchronously(new Runnable() { // Always do get request Asynchronously
            @Override
            public void run() {
                try {
                    String calendarId = URLEncoder.encode(calendarid, "UTF-8");
                    BufferedReader readStream = new BufferedReader(new InputStreamReader(new URL(appsScript + "?calendarId=" + calendarId + "&action=isOwnedByMe").openStream()));
                    String readLine; 
                    StringBuilder data = new StringBuilder(); 
                    while ((readLine = readStream.readLine()) != null) data.append(readLine); 
                    readStream.close(); 
                    final String finalData = data.toString(); 
                    form.runOnUiThread(new Runnable() { 
                        @Override
                        public void run() {
                            if (finalData == "true") {
                                CheckedIfCalendarIsYours(calendarid, true);
                            } else {
                                CheckedIfCalendarIsYours(calendarid, false);
                            }
                        }
                    });
                } catch (IOException e) {
                    Error(e.getMessage());
                }
            }
        });
    }

    @SimpleEvent(description = "Invoked when the extension has determined whether the calendar is hidden.")
    public void CheckedIfCalendarIsHidden(String calendarId, boolean isHidden) {
        EventDispatcher.dispatchEvent(this, "CheckedIfCalendarIsHidden", calendarId, isHidden);
    }

    @SimpleEvent(description = "Invoked when the extension has determined whether the calendar is your primary calendar.")
    public void CheckedIfCalendarIsPrimary(String calendarId, boolean isPrimary) {
        EventDispatcher.dispatchEvent(this, "CheckedIfCalendarIsPrimary", calendarId, isPrimary);
    }

    @SimpleEvent(description = "This event is fired when the extension has got whether the calendar is owned by you.")
    public void CheckedIfCalendarIsYours(String calendarId, boolean isOwnedByYou) {
        EventDispatcher.dispatchEvent(this, "CheckedIfCalendarIsYours", calendarId, isOwnedByYou);
    }

    @SimpleEvent(description = "This event is fired when the extension has got whether the calendar's events are displayed in your calendar.")
    public void CheckedIfCalendarIsSelected(String calendarId, boolean isSelected) {
        EventDispatcher.dispatchEvent(this, "CheckedIfCalendarIsSelected", calendarId, isSelected);
    }

    @SimpleEvent(description = "Invoked when the extension has successfully deleted the calendar with the"
            + " DeleteCalendar() method.")
    public void DeletedCalendar(String calendarId) {
        EventDispatcher.dispatchEvent(this, "DeletedCalendar", calendarId);
    }

    @SimpleEvent(description = "Invoked when the extension has successfully unsubscribed you from the calendar"
            + " with the UnsubscribeFromCalendar() method.")
    public void UnsubscribedFromCalendar(String calendarId) {
        EventDispatcher.dispatchEvent(this, "UnsubscribedFromCalendar", calendarId);
    }

    @SimpleEvent(description = "Invoked when the extension has successfully deleted the event specified by evenId"
            + " with the DeleteEvent() method.")
    public void DeletedEvent(String calendarId, String eventId) {
        EventDispatcher.dispatchEvent(this, "DeletedEvent", calendarId, eventId);
    }
}

