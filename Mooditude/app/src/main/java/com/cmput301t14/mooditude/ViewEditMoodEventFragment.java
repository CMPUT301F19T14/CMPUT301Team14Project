package com.cmput301t14.mooditude;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LifecycleOwner;

public class ViewEditMoodEventFragment extends DialogFragment {
    private ImageButton cancelButton;
    private ImageButton submitButton;
    private Spinner moodSpinner;
    private Spinner socialSituationSpinner;
    private EditText commentEditText;
    private TextView locationTextView;
    private TextView photoTextView;

    private String commentString;
    private String moodString;
    private String socialSituationString;

    private MoodEvent selectedMoodEvent;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    /** handle View/Edit of MoodEvent,
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_edit_mood_event_fragment_layout, null);
        cancelButton = view.findViewById(R.id.frag_cancel_button);
        submitButton = view.findViewById(R.id.frag_submit_button);
        moodSpinner = view.findViewById(R.id.frag_mood_spinner);
        socialSituationSpinner = view.findViewById(R.id.frag_social_situation_spinner);
        commentEditText = view.findViewById(R.id.frag_comment_edittext);
        locationTextView = view.findViewById(R.id.frag_location_textview);
        photoTextView = view.findViewById(R.id.frag_photo_textview);


        Bundle args = getArguments();
        if (args != null){
            selectedMoodEvent = (MoodEvent) args.getSerializable("moodEvent");
        }

        if (selectedMoodEvent != null) {

            moodString = selectedMoodEvent.getMood().getMood();
            socialSituationString = selectedMoodEvent.getSocialSituation().getSocialSituation();
            commentEditText.setText(selectedMoodEvent.getTextComment());

            // set dropdown moodSpinner Adapter
            ArrayAdapter<CharSequence> moodArrayAdapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.mood_string_array, android.R.layout.simple_spinner_item);
            moodArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            moodSpinner.setAdapter(moodArrayAdapter);
            moodSpinner.setSelection(moodArrayAdapter.getPosition(selectedMoodEvent.getMood().getMood()),true);
            // set moodSpinner on item select
            moodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    moodString = parent.getItemAtPosition(position).toString();
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
            final AlertDialog d = builder.setView(view)
                    .setTitle("MoodEvent")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK", null).create();

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
                                Log.i("TAG","INVALID MOOD");
                                ((TextView) moodSpinner.getSelectedView()).setError(MoodEventValidator.getErrorMessage());
                            }
                            SocialSituation socialSituation = MoodEventValidator.checkSocialSituation(socialSituationString);
                            if (socialSituation == null) {
                                valid = false;
                                Log.i("TAG","INVALID SOCIALSITUATION");
                                ((TextView) socialSituationSpinner.getSelectedView()).setError(MoodEventValidator.getErrorMessage());
                            }

                            commentString = commentEditText.getText().toString();
                            if (!MoodEventValidator.checkComment(commentString)) {
                                valid = false;
                                commentEditText.setError(MoodEventValidator.getErrorMessage());
                            }

                            if (valid) {
                                // TODO: put actual location and photo
                                MoodEvent moodEvent = new MoodEvent(1, mood,
                                        new Location(0.0, 0.0),
                                        socialSituation, commentString, selectedMoodEvent.getDatetime());


                                // push the MoodEvent to database
                                User user = new User();
                                Log.i("TAG", "Add User");
                                user.pushMoodEvent(moodEvent);
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

    static ViewEditMoodEventFragment newInstance(MoodEvent moodEvent) {
        Bundle args = new Bundle();
        args.putSerializable("moodEvent", moodEvent);

        ViewEditMoodEventFragment fragment = new ViewEditMoodEventFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
