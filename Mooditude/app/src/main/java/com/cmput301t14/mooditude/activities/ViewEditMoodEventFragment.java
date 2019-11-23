package com.cmput301t14.mooditude.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cmput301t14.mooditude.models.Location;
import com.cmput301t14.mooditude.models.Mood;
import com.cmput301t14.mooditude.models.MoodEvent;
import com.cmput301t14.mooditude.services.MoodEventValidator;
import com.cmput301t14.mooditude.R;
import com.cmput301t14.mooditude.models.SocialSituation;
import com.cmput301t14.mooditude.services.User;


/**
 * Fragment Class for Editing and Viewing MoodEvent detail, selected from the Mood History
 */
public class ViewEditMoodEventFragment extends DialogFragment {

    private Spinner moodSpinner;
    private Spinner socialSituationSpinner;
    private EditText commentEditText;
    private TextView locationTextView;
    private TextView photoTextView;

    private String commentString;
    private String moodString;
    private String socialSituationString;

    private MoodEvent selectedMoodEvent;
    private Boolean editable;

    /**
     * onAttach for the Fragment, using the super's method
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    /**
     * create the dialog of handle View/Edit of MoodEvent
     * pull the values from DB and show them
     * submit the change to DB on click ok
     * cancel goes back
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_edit_mood_event_fragment_layout, null);
        moodSpinner = view.findViewById(R.id.frag_mood_spinner);
        socialSituationSpinner = view.findViewById(R.id.frag_social_situation_spinner);
        commentEditText = view.findViewById(R.id.frag_comment_edittext);
        locationTextView = view.findViewById(R.id.frag_location_textview);
        photoTextView = view.findViewById(R.id.frag_photo_textview);


        Bundle args = getArguments();
        if (args != null){
            selectedMoodEvent = (MoodEvent) args.getSerializable("moodEvent");
            editable = (Boolean) args.getSerializable("editable");
        }

        if (selectedMoodEvent != null) {

            moodString = selectedMoodEvent.getMood().getMood();
            socialSituationString = selectedMoodEvent.getSocialSituation().getSocialSituation();
            commentEditText.setText(selectedMoodEvent.getTextComment());

            // set dropdown moodSpinner Adapter
            final ArrayAdapter<CharSequence> moodArrayAdapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.mood_string_array, android.R.layout.simple_spinner_item);
            moodArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            moodSpinner.setAdapter(moodArrayAdapter);
            moodSpinner.setSelection(moodArrayAdapter.getPosition(selectedMoodEvent.getMood().getMood()),true);
            // set moodSpinner on item select
            moodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Mood happy = new Mood("HAPPY");
                    Mood sad = new Mood("SAD");
                    Mood excited = new Mood("EXCITED");
                    Mood angry = new Mood("ANGRY");
                    String spinnerStr = parent.getItemAtPosition(position).toString();
                    if (spinnerStr.equals(happy.getEmoticon() + happy.getMood())){
                        moodString = happy.getMood();
                        view.setBackgroundColor(happy.getColor());
                    }
                    else if (spinnerStr.equals(sad.getEmoticon() + sad.getMood())){
                        moodString = sad.getMood();
                        view.setBackgroundColor(sad.getColor());
                    }
                    else if (spinnerStr.equals(excited.getEmoticon() + excited.getMood())){
                        moodString = excited.getMood();
                        view.setBackgroundColor(excited.getColor());
                    }
                    else if (spinnerStr.equals(angry.getEmoticon() + angry.getMood())){
                        moodString = angry.getMood();
                        view.setBackgroundColor(angry.getColor());
                    }
                    view.getBackground().setAlpha(50);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // nothing selected
                }
            });

            // set dropdown socialSituationSpinner Adapter
            ArrayAdapter<CharSequence> socialSituationArrayAdapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.social_situation_string_array, android.R.layout.simple_spinner_item);
            socialSituationArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            socialSituationSpinner.setAdapter(socialSituationArrayAdapter);
            socialSituationSpinner.setSelection(socialSituationArrayAdapter.getPosition(selectedMoodEvent.getSocialSituation().getSocialSituation()),true);
            // set socialSituationSpinner on item select
            socialSituationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    socialSituationString = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // nothing selected
                }
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            final AlertDialog d;
            if (editable){
                // editable, can submit by "OK" and can "Cancel"
                d = builder.setView(view)
                        .setTitle("MoodEvent")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("OK", null).create();
            }
            else{
                // not editable, can only close
                d = builder.setView(view)
                        .setTitle("MoodEvent")
                        .setNegativeButton("Close", null).create();

                // lock the fields
                this.disableEdit();
            }

            /* Use View.OnclickListener to get manual control of Dialog dismiss, only dismiss
            after all validation passed and value updated , use validator 's
            onValidationFailed and onValidationSucceeded to handle input requirement validation*/
            d.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // validate the input fields
                            boolean valid = true;

                            Mood mood = MoodEventValidator.checkMood(moodString);
                            if (mood == null) {
                                valid = false;
                                ((TextView) moodSpinner.getSelectedView()).setError(MoodEventValidator.getErrorMessage());
                            }
                            SocialSituation socialSituation = MoodEventValidator.checkSocialSituation(socialSituationString);
                            if (socialSituation == null) {
                                valid = false;
                                ((TextView) socialSituationSpinner.getSelectedView()).setError(MoodEventValidator.getErrorMessage());
                            }

                            commentString = commentEditText.getText().toString();
                            if (!MoodEventValidator.checkComment(commentString)) {
                                valid = false;
                                commentEditText.setError(MoodEventValidator.getErrorMessage());
                            }

                            if (valid) {
                                // TODO: put actual location and photo
                                MoodEvent moodEvent = new MoodEvent(mood,
                                        new Location(0.0, 0.0),
                                        socialSituation, commentString, selectedMoodEvent.getDatetime());

                                // push the MoodEvent to database
                                User user = new User();
                                user.pushMoodEvent(moodEvent);
//                                moodArrayAdapter.notifyDataSetChanged();
                                getDialog().dismiss();
                            }
                        }
                    });
                }
            });
        return d;
        }
     return null;
    }

    /**
     * lock and disable the editable fields when editable is set to false
     */
    private void disableEdit(){
        // TODO: add real location and photo
        moodSpinner.setEnabled(false);
        moodSpinner.setFocusable(false);

        socialSituationSpinner.setEnabled(false);
        socialSituationSpinner.setFocusable(false);

        commentEditText.setEnabled(false);
        commentEditText.setInputType(InputType.TYPE_NULL);
        commentEditText.setFocusable(false);

        locationTextView.setEnabled(false);
        locationTextView.setInputType(InputType.TYPE_NULL);
        locationTextView.setFocusable(false);

        photoTextView.setEnabled(false);
        photoTextView.setInputType(InputType.TYPE_NULL);
        photoTextView.setFocusable(false);
    }

    /**
     * Constructor like method, get the parameters passed in as Bundle
     * @param moodEvent - the MoodEvent selected in Mood History
     * @return
     */

    static ViewEditMoodEventFragment newInstance(MoodEvent moodEvent, Boolean editable) {
        Bundle args = new Bundle();
        args.putSerializable("moodEvent", moodEvent);
        args.putSerializable("editable", editable);

        ViewEditMoodEventFragment fragment = new ViewEditMoodEventFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
