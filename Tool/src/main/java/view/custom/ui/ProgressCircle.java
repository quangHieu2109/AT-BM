package view.custom.ui;


import okhttp3.Response;

import javax.swing.*;


public abstract class ProgressCircle extends SwingWorker<Response, Void> {
    public ProgressCircleDialog progressCircleDialog;

    public ProgressCircle(JFrame frame) {
        progressCircleDialog = new ProgressCircleDialog(frame);
    }

    @Override
    protected Response doInBackground() throws Exception {
        progressCircleDialog.showProgress(true);
        return null;
    }

    @Override
    protected void done() {
        progressCircleDialog.dispose();
    }
}
