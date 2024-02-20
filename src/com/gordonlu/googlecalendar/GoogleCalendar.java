package com.gordonlu.googlecalendar;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;

import java.io.ByteArrayOutputStream;
import com.google.appinventor.components.runtime.util.AsynchUtil;
import java.io.BufferedReader;
import java.lang.StringBuilder;
import java.io.InputStreamReader;
import java.net.URL;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import androidx.core.graphics.ColorUtils;
import android.graphics.Color;
import java.lang.Integer;
import java.util.concurrent.TimeUnit;
import java.util.Calendar;
import com.google.appinventor.components.runtime.util.Dates;
import com.google.appinventor.components.runtime.util.YailList;
import com.google.appinventor.components.runtime.util.CsvUtil;

@DesignerComponent(version = 1, description = "An extension that allows you to manage your Google Calendars in App Inventor. Created by Gordon.",
 iconName = "aiwebres/calendar.png", category = ComponentCategory.EXTENSION, nonVisible = true)
@SimpleObject(external = true)

public class GoogleCalendar extends AndroidNonvisibleComponent {

    String calendarid = "";
    String appsScript = "";

    public GoogleCalendar(ComponentContainer container){
        super(container.$form());
    }

    @DesignerProperty(editorType = "string", defaultValue = "")
    @SimpleProperty(description = "Specifies the URL to your Google Apps Script.")
    public void ScriptUrl(String s) {
        appsScript = s;
    }
    @DesignerProperty(editorType = "string", defaultValue = "")
    @SimpleProperty(description = "Specifies the calendar ID of your Google Calendar.")
    public void CalendarId(String i) {
        calendarid = i;
    }
    @SimpleProperty(description = "Specifies the calendar ID of your Google Calendar.")
    public String CalendarId() {
        return calendarid;
    }
    @SimpleProperty(description = "Specifies the URL to your Google Apps Script.")
    public String ScriptUrl() {
        return appsScript;
    }
    @SimpleFunction(description = "Gets the timezone of your calendar.")
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
    @SimpleFunction(description = "Gets the description of your calendar and fires the GotCalendarDescription event after this.")
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
    @SimpleFunction(description = "Lists a list of all of your calendars and fires the ListedCalendars event after this.")
    public void ListAllCalendars(){
        AsynchUtil.runAsynchronously(new Runnable() { // Always do get request Asynchronously
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
    @SimpleFunction(description = "Unsubscribes from this calendar. You cannot unsubscribe from a calendar that you own.")
    public void UnsubscribeFromCalendar(){
        AsynchUtil.runAsynchronously(new Runnable() { // Always do get request Asynchronously
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
    @SimpleFunction(description = "Lists all of your calendars that are owned by you and fires the ListedOwnedCalendars event.")
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
    @SimpleFunction(description = "Lists all of the creators of this event and fires the ListedEventCreators event.")
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
    @SimpleFunction(description = "Gets the color of your calendar and fires the GotCalendarColor event after this.")
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
    @SimpleFunction(description = "Sets the color of your calendar and fires the AfterCalendarColorSet event after this.")
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
    @SimpleFunction(description = "Gets the timezone of your calendar.")
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
    @SimpleFunction(description = "Adds a guest to your event.")
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
    @SimpleFunction(description = "Creates a new calendar and fires the CreatedCalendar event after this. The CalendarId property has no effect on this block.")
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
    @SimpleFunction(description = "Sets whether the calendar should be hidden among the user interface of your Calendar app. You cannot hide your personal default calendar.")
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
    @SimpleFunction(description = "Sets whether the calendar should be hidden among the user interface of your Calendar app. You cannot hide your personal default calendar." + 
    " The difference between this and SetCalendarHidden is that SetCalendarHidden not only (un)hide the events for this calendar in the large dashboard, but also" + 
    " hides the calendar name from the list of calendar options.")
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
    @SimpleEvent(description = "This event is fired after the timezone of your calendar is gotten.")
    public void GotCalendarTimeZone(String calendarId, String timezone){
        EventDispatcher.dispatchEvent(this, "GotCalendarTimeZone", calendarId, timezone);
    }
    @SimpleEvent(description = "This event is fired after the color of your calendar is gotten.")
    public void GotCalendarColor(String calendarId, int color){
        EventDispatcher.dispatchEvent(this, "GotCalendarColor", calendarId, color);
    }
    @SimpleEvent(description = "This event is fired after the extension has set the new timezone of your calendar.")
    public void AfterCalendarTimezoneSet(String calendarId, String newTimezone){
        EventDispatcher.dispatchEvent(this, "AfterCalendarTimezoneSet", calendarId, newTimezone);
    }
    @SimpleEvent(description = "This event is fired after the extension has added a guest to your event.")
    public void AddedGuestToEvent(String calendarId, String eventId, String guestEmail){
        EventDispatcher.dispatchEvent(this, "AddedGuestToEvent", calendarId, eventId, guestEmail);
    }
    @SimpleEvent(description = "This event is fired after the extension has set the new color of your calendar.")
    public void AfterCalendarColorSet(String calendarId, int color){
        EventDispatcher.dispatchEvent(this, "AfterCalendarColorSet", calendarId, color);
    }
    @SimpleEvent(description = "This event is fired after the extension has hidden or revealed your calendar in the user interface.")
    public void AfterCalendarSetIfHidden(String calendarId, boolean isHidden){
        EventDispatcher.dispatchEvent(this, "AfterCalendarSetIfHidden", calendarId, isHidden);
    }
    @SimpleEvent(description = "This event is fired after the extension has hidden or revealed your calendar in the user interface.")
    public void AfterCalendarSetIfSelected(String calendarId, boolean isSelected){
        EventDispatcher.dispatchEvent(this, "AfterCalendarSetIfSelected", calendarId, isSelected);
    }
    @SimpleEvent(description = "This event is fired after the extension has received a list of your calendars.")
    public void ListedCalendars(String calendarId, YailList calendars){
        EventDispatcher.dispatchEvent(this, "ListedCalendars", calendarId, calendars);
    }
    @SimpleEvent(description = "This event is fired after the extension has received a list of the creators of this event.")
    public void ListedEventCreators(String calendarId, String eventId, YailList creators){
        EventDispatcher.dispatchEvent(this, "ListedEventCreators", calendarId, eventId, creators);
    }
    @SimpleEvent(description = "This event is fired after the extension has received a list of your owned calendars.")
    public void ListedOwnedCalendars(String calendarId, YailList calendars){
        EventDispatcher.dispatchEvent(this, "ListedOwnedCalendars", calendarId, calendars);
    }
    @SimpleEvent(description = "This event is fired after the extension has set the new color of your calendar.")
    public void AfterCalendarDescriptionSet(String calendarId, String newDescription){
        EventDispatcher.dispatchEvent(this, "AfterCalendarDescriptionSet", calendarId, newDescription);
    }
    @SimpleEvent(description = "This event is fired when an error has occurred.")
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
    @SimpleFunction(description = "Creates an all day event that only covers a single day without custom options and fires the CreatedSingleWholeDayEvent after this.")
    public void CreateSingleWholeDayEvent(final String title, Calendar date){
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
    @SimpleFunction(description = "Creates an all day event that only covers a single day with custom options. The guests parameters should be a list of the email addresses of the guest that" + 
    " you want to add to this event, and sendInvites indicates whether the calendar should send invite emails. Default is false. Fires the CreatedSingleWholeDayEvent event after this.")
    public void CreateSingleWholeDayEventWithOptions(final String title, Calendar date, final String description, final String location, final YailList guests, final boolean sendInvites){
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
    @SimpleFunction(description = "Creates an all day event that covers dates from the start date to the end date inclusively without custom options.")
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
    @SimpleFunction(description = "Creates an all day event that covers dates from the start date to the end date inclusively with custom options.")
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
    @SimpleFunction(description = "Attempts to get the name of the calendar and fires the GotName event after this.")
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
    @SimpleFunction(description = "Deletes the calendar and fires the DeletedEvent after this.")
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
    @SimpleFunction(description = "Deletes the event and fires the DeletedCalendar after this.")
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
    @SimpleEvent(description = "This event is fired when the extension has created a new calendar.")
    public void CreatedCalendar(String calendarId, String calendarName) {
        EventDispatcher.dispatchEvent(this, "CreatedCalendar", calendarId, calendarName);
    }
    @SimpleEvent(description = "This event is fired when the extension has got the name of the calendar.")
    public void GotCalendarName(String calendarId, String name) {
        EventDispatcher.dispatchEvent(this, "GotCalendarName", calendarId, name);
    }
    @SimpleEvent(description = "This event is fired when the extension has got the calendar ID of the default calendar.")
    public void GotDefaultCalendarId(String id) {
        EventDispatcher.dispatchEvent(this, "GotDefaultCalendarId", id);
    }
    @SimpleEvent(description = "This event is fired when the extension has got the description of the calendar.")
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
    @SimpleFunction(description = "Attempts to check if the calendar is owned by you and fires the CheckedIfCalendarIsYours after this.")
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
    @SimpleEvent(description = "This event is fired when the extension has got whether the calendar is hiddden in your user interface.")
    public void CheckedIfCalendarIsHidden(String calendarId, boolean isHidden) {
        EventDispatcher.dispatchEvent(this, "CheckedIfCalendarIsHidden", calendarId, isHidden);
    }
    @SimpleEvent(description = "This event is fired when the extension has got whether the calendar is your primary calendar.")
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
    @SimpleEvent(description = "This event is fired when the extension has deleted the calendar.")
    public void DeletedCalendar(String calendarId) {
        EventDispatcher.dispatchEvent(this, "DeletedCalendar", calendarId);
    }
    @SimpleEvent(description = "This event is fired when the extension has unsubscribed you from the calendar.")
    public void UnsubscribedFromCalendar(String calendarId) {
        EventDispatcher.dispatchEvent(this, "UnsubscribedFromCalendar", calendarId);
    }
    @SimpleEvent(description = "This event is fired when the extension has deleted the event.")
    public void DeletedEvent(String calendarId, String eventId) {
        EventDispatcher.dispatchEvent(this, "DeletedEvent", calendarId, eventId);
    }
}

