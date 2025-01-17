package com.nubank.allan.billscreen.controller.handler;

import android.content.Context;
import android.util.Log;

import com.nubank.allan.billscreen.model.Bill;
import com.nubank.allan.billscreen.model.LineItem;
import com.nubank.allan.billscreen.model.Summary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Allan on 12/01/2016.
 */

public class JSONHandler {

    private Context context;

    public JSONHandler(Context context) {
        this.context = context;
    }

    public ExceptionHandler createExceptionHandler() {
        return new ExceptionHandler();
    }

    // Parses JSONObject -> Summary
    public Summary parseJSONObjectToSummary(JSONObject summaryObject) throws JSONException, ParseException {

        Summary summary = new Summary();

        // Summary - Date handling
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.sql.Date date;
        double temp = 0.0;

        // Get the data from JSON
        if (summaryObject.has("close_date")) {
            date = new java.sql.Date(format.parse(summaryObject.getString("close_date")).getTime());
            summary.setCloseDate(date);
        }
        if (summaryObject.has("due_date")) {
            date = new java.sql.Date(format.parse(summaryObject.optString("due_date")).getTime());
            summary.setDueDate(date);
        }
        if (summaryObject.has("open_date")) {
            date = new java.sql.Date(format.parse(summaryObject.optString("open_date")).getTime());
            summary.setOpenDate(date);
        }
        if (summaryObject.has("past_balance")) {
            temp = (double) summaryObject.getInt("past_balance") / 100;
            summary.setPastBalance(temp);
        }
        if (summaryObject.has("total_balance")) {
            temp = (double) summaryObject.getInt("total_balance") / 100;
            summary.setTotalBalance(temp);
        }
        if (summaryObject.has("total_cumulative")) {
            temp = (double) summaryObject.getInt("total_cumulative") / 100;
            summary.setTotalCumulative(temp);
        }
        if (summaryObject.has("interest")) {
            temp = (double) summaryObject.getInt("interest") / 100;
            summary.setInterest(temp);
        }
        if (summaryObject.has("paid")) {
            temp = (double) summaryObject.getInt("paid") / 100;
            summary.setPaid(temp);
        }
        if (summaryObject.has("minimum_payment")) {
            temp = (double) summaryObject.getInt("minimum_payment") / 100;
            summary.setMinPayment(temp);
        }


        return summary;
    }

    // Parses JSONObject -> ArrayList<LineItem>
    public ArrayList<LineItem> parseJSONArrayToLineItem(JSONArray lineItemArray) throws JSONException, ParseException {

        // Variables
        ArrayList<LineItem> lineItems = new ArrayList<>();
        int size = lineItemArray.length();

        // Foreach LineItem in JSONArray
        for (int i = 0; i < size; i++) {
            JSONObject lineItemObject = lineItemArray.getJSONObject(i);
            LineItem temp = new LineItem();
            double double_temp = 0.0;

            // Get data from JSON and set temp variable
            if (lineItemObject.has("post_date")) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date date = new java.sql.Date(format.parse(lineItemObject.get("post_date").toString()).getTime());
                temp.setPostDate(date);
            }
            if (lineItemObject.has("amount")) {
                double_temp = (double) lineItemObject.getInt("amount") / 100;
                temp.setAmount(double_temp);
            }
            if (lineItemObject.has("title")) {
                temp.setTitle(lineItemObject.get("title").toString());
            }
            if (lineItemObject.has("index")) {
                temp.setIndex(lineItemObject.getInt("index"));
            }
            if (lineItemObject.has("charges")) {
                temp.setCharges(lineItemObject.getInt("charges"));
            }
            if (lineItemObject.has("href")) {
                temp.setHref(lineItemObject.getString("href"));
            }
            // Adds temp variable to ArrayList
            lineItems.add(temp);

        }

        return lineItems;
    }

    // Parses JSONObject -> Bill
    public Bill parseJSONObjectToBill(JSONObject json){

        ExceptionHandler ex = createExceptionHandler();

        // Objects to mount bill
        Bill bill = new Bill();
        Summary summary;
        ArrayList<LineItem> lineItems;
        HashMap<String, String> links = new HashMap<>();

        try {
            // JSONObjects from file
            JSONObject billObject = json.getJSONObject("bill");
            JSONObject summaryObject = billObject.getJSONObject("summary");
            JSONObject linksObject = billObject.getJSONObject("_links");
            JSONArray lineItemArray = null;

            lineItemArray = billObject.getJSONArray("line_items");

            // Summary
            summary = parseJSONObjectToSummary(summaryObject);

            // LineItem
            lineItems = parseJSONArrayToLineItem(lineItemArray);

            // Links
            if (linksObject.has("self")) {
                links.put("self", linksObject.getJSONObject("self").optString("href"));
            }
            if (linksObject.has("boleto_email")) {
                links.put("boleto_email", linksObject.getJSONObject("boleto_email").optString("href"));
            }
            if (linksObject.has("barcode")) {
                links.put("barcode", linksObject.getJSONObject("barcode").optString("href"));
            }

            // Bill attributes
            bill.setState(billObject.optString("state"));
            bill.setId(billObject.optString("id"));
            bill.setBarCode(billObject.optString("barcode"));
            bill.setDigitableLine(billObject.optString("linha_digitavel"));
            bill.setSummary(summary);
            bill.setLinks(links);
            bill.setItems(lineItems);
        }
        catch (ParseException e) {
            ex.showErrorActivity(context, "err_parse");
        }
        catch (JSONException e) {
            ex.showErrorActivity(context, "err_json");
        }

        return bill;
    }
}
