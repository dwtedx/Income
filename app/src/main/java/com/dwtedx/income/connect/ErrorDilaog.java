package com.dwtedx.income.connect;

import android.app.Activity;
import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dwtedx.income.R;


/**
 * @Created by dwtedx(qinyl)(http://dwtedx.com) on 15/12/21 上午9:12.
 * @Company 路之遥网络科技有限公司
 * @Description 定义应用中涉及的所有错误
 */
public class ErrorDilaog {
    private static boolean isShowDilaog = false;

    public static void setIsShowDilaog(boolean isShow) {
        isShowDilaog = isShow;
    }

    /**
     * @Description 显示错误日志
     * @param activity
     * @param e
     */
    public static void showErrorDiloag(final Activity activity, final SaException e) {
        if (!isShowDilaog) {
            isShowDilaog = true;

            String message = activity.getResources().getString(R.string.err_msg_unkown);

            switch (e.getErrorType()){
                case SaError.SAERROR_TYPE_LOCAL :
                    switch (e.getErrorCode()) {
                        case SaError.ERROR_TYPE_MALFORMED_URL:
                            message = activity.getResources().getString(R.string.err_msg_malformed);
                            break;

                        case SaError.ERROR_TYPE_PROTOCOL:
                            message = activity.getResources().getString(R.string.err_msg_client_protocl);
                            break;

                        case SaError.ERROR_TYPE_SOCKET:
                            message = activity.getResources().getString(R.string.err_msg_socket);
                            break;

                        case SaError.ERROR_TYPE_IO:
                            message = activity.getResources().getString(R.string.err_msg_io);
                            break;

                        case SaError.ERROR_TYPE_UNKNOWN:
                            message = activity.getResources().getString(R.string.err_msg_unkown);
                            break;

                        case SaError.ERROR_TYPE_JSON:
                            message = activity.getResources().getString(R.string.err_msg_json);
                            break;

                        case SaError.ERROR_TYPE_TIMEOUT:
                            message = activity.getResources().getString(R.string.err_msg_timeout);
                            break;

                        case SaError.ERROR_TYPE_EOF:
                            message = activity.getResources().getString(R.string.err_msg_eof);
                            break;
                    }
                    break;
                case SaError.SAERROR_TYPE_COMUNICATION :
                    message = e.getErrorMessage();
                    break;
                case SaError.SAERROR_TYPE_REMOTE :
                    message = e.getErrorMessage();
                    break;
            }

            new MaterialDialog.Builder(activity)
                    .title(R.string.tip)
                    .content(message)
                    .positiveText(R.string.ok)
                    .negativeText(R.string.cancel)
                    .cancelable(false)
                    .onAny(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            isShowDilaog = false;
                        }
                    })
                    .show();
        }
    }
}
