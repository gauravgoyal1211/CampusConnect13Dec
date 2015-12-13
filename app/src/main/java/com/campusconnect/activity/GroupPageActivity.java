package com.campusconnect.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.campusconnect.R;
import com.campusconnect.adapter.GroupPageAdapterActivity;
import com.campusconnect.adapter.GroupPage_infoActivity;
import com.campusconnect.bean.GroupBean;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.utility.NetworkAvailablity;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GroupPageActivity extends ActionBarActivity {

    RecyclerView group_page;

    private static final String LOG_TAG = "GroupPageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);

        group_page = (RecyclerView) findViewById(R.id.recycler_group_page);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        group_page.setLayoutManager(llm);
        group_page.setHasFixedSize(true);
        group_page.setItemAnimator(new DefaultItemAnimator());
        GroupBean bean = (GroupBean) getIntent().getSerializableExtra("BEAN");
        String club_id = bean.getClubId();
        WebApiGetGroup(club_id);


    }


    private List<GroupPage_infoActivity> createList_group_page(int size) {

        List<GroupPage_infoActivity> result = new ArrayList<GroupPage_infoActivity>();
        for (int i = 1; i <= size; i++) {
            GroupPage_infoActivity ci = new GroupPage_infoActivity();

            result.add(ci);

        }

        return result;
    }

    public void WebApiGetGroup(String club_id) {
        if (NetworkAvailablity.hasInternetConnection(GroupPageActivity.this)) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("club_id", "" + club_id);
                //     jsonObject.put("pid", "" + pid);
                List<NameValuePair> param = new ArrayList<NameValuePair>();
                String url = WebServiceDetails.DEFAULT_BASE_URL + "getClub";
                Log.e("getGroup", jsonObject.toString());
                Log.e("", url);

                new WebRequestTask(GroupPageActivity.this, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_GET_CLUB,
                        true, url).execute();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(GroupPageActivity.this, "Network is not available.", Toast.LENGTH_SHORT).show();
        }
    }

    private final Handler _handler = new Handler() {
        public void handleMessage(Message msg) {
            int response_code = msg.what;

            if (response_code != 0 && response_code != 204) {
                String strResponse = (String) msg.obj;
                Log.v("Response", strResponse);
                if (strResponse != null && strResponse.length() > 0) {

                    switch (response_code) {
                        case WebServiceDetails.PID_GET_CLUB: {
                            try {
                                JSONObject grpJson = new JSONObject(strResponse);
                                String kind = grpJson.optString("kind");
                                String etag = grpJson.optString("etag");
                                String description = grpJson.optString("description");
                                String admin = grpJson.optString("admin");
                                String clubId = grpJson.optString("club_id");
                                String abb = grpJson.optString("abbreviation");
                                String name = grpJson.optString("name");
                                String photoUrl = grpJson.optString("photoUrl");
                                String followcount = grpJson.optString("followercount");
                                String member_count = grpJson.optString("membercount");


                                GroupBean bean = new GroupBean();
                                bean.setAbb(abb);
                                bean.setName(name);
                                bean.setAdmin(admin);
                                bean.setClubId(clubId);
                                bean.setFollow("0");
                                bean.setDescription(description);
                                bean.setPhotourl(photoUrl);
                                bean.setMemberCount(member_count);
                                bean.setFollowCount(followcount);
                                GroupPageAdapterActivity gp = new GroupPageAdapterActivity(createList_group_page(5), bean, GroupPageActivity.this);
                                group_page.setAdapter(gp);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }
            }
        }
    };
}
