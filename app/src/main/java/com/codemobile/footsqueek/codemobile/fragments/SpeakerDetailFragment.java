package com.codemobile.footsqueek.codemobile.fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.database.Session;
import com.codemobile.footsqueek.codemobile.database.Speaker;
import com.codemobile.footsqueek.codemobile.services.CircleCroppedBitmap;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import io.realm.Realm;

/**
 * Created by greg on 23/01/2017.
 */

public class SpeakerDetailFragment extends Fragment {

    TextView nameTv,talkTv,bioTv;
    ImageView imageView;
    String speakerId = "-1";
    Context mContext;


    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_speaker_detail,container,false);

        Bundle extras = getArguments();
        speakerId = extras.getString("id");

        nameTv = (TextView)view.findViewById(R.id.speakerName);
        talkTv = (TextView)view.findViewById(R.id.speakerTalk);
        bioTv = (TextView)view.findViewById(R.id.speakerBio);
        imageView = (ImageView)view.findViewById(R.id.speakerImage);

        setViews();

        return view;
    }

    private void setViews(){

        Speaker speaker;
        if (speakerId.equals("-1")) {
            nameTv.setText("Error getting talk ");
        } else {
            Realm realm = AppDelegate.getRealmInstance();
            speaker = realm.where(Speaker.class).equalTo("id", speakerId).findFirst();
                        Session session = realm.where(Session.class).equalTo("speakerId",speakerId).findFirst();

            setImage(speaker);

            nameTv.setText(speaker.getFirstname());
            talkTv.setText(session.getTitle());
            bioTv.setText(speaker.getProfile()+ "Antony was a supporter of Julius Caesar, and served as one of his generals during the conquest of Gaul and the Civil War. Antony was appointed administrator of Italy while Caesar eliminated political opponents in Greece, North Africa, and Spain. After Caesar's death in 44 BC, Antony joined forces with Marcus Aemilius Lepidus, another of Caesar's generals, and Octavian, Caesar's nephew and adopted son, forming a three-man dictatorship known to historians as the Second Triumvirate. The Triumvirs defeated Caesar's murderers, the Liberatores, at the Battle of Philippi in 42 BC, and divided the government of the Republic between themselves. Antony was assigned Rome's eastern provinces, including the client kingdom of Egypt, then ruled by Cleopatra VII Philopator, and was given the command in Rome's war against Parthia.");
        }


    }

    private void setImage(Speaker speaker){

        Picasso.with(mContext)
                .load(speaker.getPhotoUrl()).fit().centerCrop()
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                        CircleCroppedBitmap circleCroppedBitmap = new CircleCroppedBitmap(imageBitmap, mContext);
                        circleCroppedBitmap.createRoundImage(imageView);
                    }

                    @Override
                    public void onError() {

                    }
                });

    }
}
