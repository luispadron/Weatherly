package lpadron.me.weatherly.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import lpadron.me.weatherly.R;

/**
 * Created by Luis Padron on 11/4/15.
 */
public class ReportHttpErrorFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.http_error_title))
                .setMessage(context.getString(R.string.http_error_message))
                .setPositiveButton("Ok", null);
        AlertDialog dialog = builder.create();

//            case 2:
//                builder.setTitle(context.getString(R.string.network_error_title))
//                    .setMessage(context.getString(R.string.network_error_message))
//                    .setPositiveButton("Ok", null);
//                dialog = builder.create();
//                break;
//            default:
//                builder.setTitle(context.getString(R.string.unkown_error_title))
//                        .setMessage(context.getString(R.string.unknown_error_message))
//                        .setPositiveButton("Ok", null);
//                dialog = builder.create();
//                break;
//        }
        return dialog;
    }
}
