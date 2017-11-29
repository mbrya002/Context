package apps.matts.contextlearning;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static apps.matts.contextlearning.Level.hint;
import static apps.matts.contextlearning.Level.msgString;

public class MyAlertDialogFragment2 extends DialogFragment {

        public interface QuestionInterface {
            void loadNextQuestion();
        }

        QuestionInterface callback;
        TextView msg;
        //Button hintBtn;
        Button levelsbtn;
        Button nextbtn;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            callback = (QuestionInterface) context;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.feed_back_dialog, container, false);

            msg = (TextView) v.findViewById(R.id.message);
            //hintBtn = (Button) v.findViewById(R.id.hint);
            levelsbtn = (Button) v.findViewById(R.id.Levels);
            nextbtn = (Button) v.findViewById(R.id.Next);
            msg.setText(msgString);

            return v;
        }


        @Override
        public void onStart() {
            super.onStart();
            /*
            hintBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), hint,
                            Toast.LENGTH_LONG).show();
                    dismiss();
                }
            });
            */
            levelsbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), StageSelect.class);
                    intent.putExtra("Difficulty", 0);
                    startActivity(intent);
                    dismiss();
                }
            });
            nextbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.loadNextQuestion();
                    dismiss();
                }
            });

        }
    }