package com.campusconnect.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.campusconnect.R;
import com.campusconnect.activity.GroupPageActivity;
import com.campusconnect.bean.GroupBean;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.utility.SharedpreferenceUtility;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by RK on 11-09-2015.
 */
public class ProfilePageAdapterActivity extends
        RecyclerView.Adapter<ProfilePageAdapterActivity.GroupsViewHolder> {

    List<GroupBean> GroupsJoinedList;
    int posi = 0;
    ProfileInfoViewHolder holder1;
    ImageView profile_image;
    TextView profile_name, profile_tags, tv_branch, tv_batch_of;
    GroupsJoinedListHolder holder2;
    Context context;

    public ProfilePageAdapterActivity(List<GroupBean> GroupsJoinedList, Context context) {
        this.GroupsJoinedList = GroupsJoinedList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return GroupsJoinedList.size();
    }

    @Override
    public void onBindViewHolder(GroupsViewHolder groupViewHolder, int i) {
        if (getItemViewType(i) == 0) {
            holder1 = (ProfileInfoViewHolder) groupViewHolder;
            //Log.d("name",SharedpreferenceUtility.getInstance(context).getString(AppConstants.PERSON_NAME));
            String url = SharedpreferenceUtility.getInstance(context).getString(AppConstants.PHOTO_URL);
            Picasso.with(context).load(url).into(profile_image);
            profile_name.setText(SharedpreferenceUtility.getInstance(context).getString(AppConstants.PERSON_NAME));
            tv_branch.setText(SharedpreferenceUtility.getInstance(context).getString(AppConstants.BRANCH));
            tv_batch_of.setText(SharedpreferenceUtility.getInstance(context).getString(AppConstants.BATCH));
        } else {
            if (GroupsJoinedList.size() > 0) {
                holder2 = (GroupsJoinedListHolder) groupViewHolder;
                holder2.group_joined.setText(this.GroupsJoinedList.get(i - 1).getName());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        if (position == 0)
            viewType = 0;
        else
            viewType = 1;

        return viewType;
    }

    @Override
    public GroupsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        GroupsViewHolder holder;

        if (i == 0) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.activity_card_layout_profile_info, viewGroup, false);

            holder = new ProfileInfoViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.activity_card_layout_groups_joined, viewGroup, false);
            holder = new GroupsJoinedListHolder(itemView);
        }

        return holder;
    }

    public static class GroupsViewHolder extends RecyclerView.ViewHolder {
        TextView group_joined;

        public GroupsViewHolder(View v) {
            super(v);
            group_joined = (TextView) v.findViewById(R.id.tv_group_joined_name);
        }

    }

    public class GroupsJoinedListHolder extends ProfilePageAdapterActivity.GroupsViewHolder {
        CardView card_g_joined;

        public GroupsJoinedListHolder(View itemView) {
            super(itemView);
            card_g_joined = (CardView) itemView.findViewById(R.id.cv_groups_joined);

            card_g_joined.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent_temp = new Intent(v.getContext(), GroupPageActivity.class);
                    posi = getPosition() - 1;

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("BEAN",GroupsJoinedList.get(posi));
                  //  bundle.putString("G_ID", (String) ProfilePageAdapterActivity.this.GroupsJoinedList.get(posi));
                    intent_temp.putExtras(bundle);

                    v.getContext().startActivity(intent_temp);

                }
            });

        }
    }

    public class ProfileInfoViewHolder extends ProfilePageAdapterActivity.GroupsViewHolder {

        public ProfileInfoViewHolder(View itemView) {
            super(itemView);
            ImageView gmail_icon = (ImageView) itemView.findViewById(R.id.iv_gmail_icon);

            gmail_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    String email = SharedpreferenceUtility.getInstance(v.getContext()).getString(AppConstants.EMAIL_KEY);
                    sendIntent.setType("plain/text");
                    sendIntent.setData(Uri.parse(email));
                    sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                    sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                    v.getContext().startActivity(sendIntent);
                }
            });

            profile_name = (TextView) itemView.findViewById(R.id.profile_name);
            profile_tags = (TextView) itemView.findViewById(R.id.profile_tag);
            tv_branch = (TextView) itemView.findViewById(R.id.tv_branch);
            tv_batch_of = (TextView) itemView.findViewById(R.id.tv_batch_of);
            profile_image = (ImageView) itemView.findViewById(R.id.profile_image);
        }
    }

}