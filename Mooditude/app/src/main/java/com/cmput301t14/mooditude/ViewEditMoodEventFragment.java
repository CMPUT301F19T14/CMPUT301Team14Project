package com.cmput301t14.mooditude;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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

public class ViewEditMoodEventFragment extends DialogFragment {
    private ImageButton submitButton;
    private Spinner moodSpinner;
    private Spinner socialSituationSpinner;
    private EditText commentEditText;
    private TextView locationTextView;
    private TextView photoTextView;

    private String commentString;
    private String moodString;
    private String socialSituationString;

    private MoodEventValidator validator;

    private OnFragmentInteractionListener listener;
    private MoodEvent selectedMoodEvent;

    public interface OnFragmentInteractionListener {
        void onOkPressed(MoodEvent selectedMoodEvent);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener");
        }
    }

    /** Dialog to handle View/Edit of MoodEvent,
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_edit_mood_event_fragment_layout, null);
        submitButton = view.findViewById(R.id.submit_button);
        moodSpinner = view.findViewById(R.id.mood_spinner);
        socialSituationSpinner = view.findViewById(R.id.social_situation_spinner);
        commentEditText = view.findViewById(R.id.comment_edittext);
        locationTextView = view.findViewById(R.id.location_textview);
        photoTextView = view.findViewById(R.id.photo_textview);


        Bundle args = getArguments();
        if (args != null){
            selectedMoodEvent = (MoodEvent) args.getSerializable("MoodEvent");
        }

        if (selectedMoodEvent != null) {

            commentEditText.setText(selectedMoodEvent.getTextComment());

            // set dropdown moodSpinner Adapter
            ArrayAdapter<CharSequence> moodArrayAdapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.mood_string_array, android.R.layout.simple_spinner_item);
            moodArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            moodSpinner.setAdapter(moodArrayAdapter);
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

        }

        validator = new MoodEventValidator();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final AlertDialog d = builder.setView(view)
                .setTitle("Add/Edit Ride")
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
                        validator.validate();
                    }
                });
            }
        });

        return d;

    }


    static ViewEditMoodEventFragment newInstance(MoodEvent moodEvent) {
        Bundle args = new Bundle();
        args.putSerializable("moodEvent", moodEvent);

        ViewEditMoodEventFragment fragment = new ViewEditMoodEventFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
