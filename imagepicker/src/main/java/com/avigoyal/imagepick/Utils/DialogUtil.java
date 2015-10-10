package com.avigoyal.imagepick.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.avigoyal.imagepick.R;


/**
 * Class to show dialog
 */
public class DialogUtil {


    /**
     * show a dialog
     *
     * @param context                ui context
     * @param view                   view to be attached in this dialog
     * @param isCancelOnTouchOutside boolean to set dialog to dismiss touch on outside
     */
    public static AlertDialog makeDialog(final Context context,
                                         final View view,
                                         boolean isCancelOnTouchOutside) {

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setCanceledOnTouchOutside(isCancelOnTouchOutside);

        // add custom view in dialog
        alertDialog.setView(view);

        return alertDialog;
    }

    /**
     * Create dialog alert according the app theme
     *
     * @param context              Context
     * @param title                Title of dialog
     * @param description          Description of dialog
     * @param negativeButtonText   Negative button text if want to show else pass null
     * @param positiveButtonText   Positive button text if want to show else pass null
     * @param isCancelTouchOutside If cancel want to cancel when touch outside pass true else pass false
     * @param listener             Listener for dialog cancel
     */
    public static void showAlertDialog(Context context,
                                       String title,
                                       String description,
                                       String negativeButtonText,
                                       String positiveButtonText,
                                       boolean isCancelTouchOutside,
                                       final OnDialogButtonClickListener listener) {

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setCanceledOnTouchOutside(isCancelTouchOutside);

        // inflate custom view
        View customView = LayoutInflater.from(context).inflate(R.layout.alert_dialog, null);
        // set dialog title
        TextView txvDialogTitle = (TextView) customView.findViewById(R.id.txv_alert_dialog_title);
        txvDialogTitle.setText(title);

        TextView dialogDesc = (TextView) customView.findViewById(R.id.txv_alert_dialog_description);
        dialogDesc.setText(description);
        // set listener on CANCEL button
        Button btnCancel = (Button) customView.findViewById(R.id.btn_alert_dialog_cancel);
        if (negativeButtonText != null) {
            btnCancel.setText(negativeButtonText);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    if (listener != null) {
                        listener.onDialogButtonClick(false);
                    }
                }
            });
        } else {
            btnCancel.setVisibility(View.GONE);
        }
        // set listener on DONE button
        Button btnDone = (Button) customView.findViewById(R.id.btn_alert_dialog_done);
        if (positiveButtonText != null) {
            btnDone.setText(positiveButtonText);
            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    if (listener != null) {
                        listener.onDialogButtonClick(true);
                    }
                }
            });
        } else {
            btnDone.setVisibility(View.GONE);
        }
        // add custom view in dialog
        alertDialog.setView(customView);
        // show dialog
        alertDialog.show();
    }


}
