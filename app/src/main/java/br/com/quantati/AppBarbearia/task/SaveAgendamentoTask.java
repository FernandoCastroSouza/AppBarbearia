package br.com.quantati.AppBarbearia.task;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import br.com.quantati.AppBarbearia.dao.AgendamentoDAO;
import br.com.quantati.AppBarbearia.ws.WebRequest;
import br.com.quantati.AppBarbearia.model.Agendamento;


/**
 * Created by Fernando on 12/06/2017.
 */
public class SaveAgendamentoTask extends AsyncTask<String, Object, Long> {
    private static final String ID = "id";
    private final Agendamento agendamento;
    private Activity activity;
    private ProgressDialog progress;

    public SaveAgendamentoTask(Activity activity, Agendamento agendamento) {
        this.activity = activity;
        this.agendamento = agendamento;
    }

    @Override
    protected void onPreExecute() {
        progress = ProgressDialog.show(activity, "Aguarde...", "Enviando dados!!!", true);
    }

    @Override
    protected Long doInBackground(String... params) {
        try {
            WebRequest request = new WebRequest();
            String jsonResult = request.save(agendamento);
            JSONObject jsonObject = new JSONObject(jsonResult);
            return jsonObject.getLong(ID);
        } catch (Exception e) {
            return 0L;
        }
    }

    @Override
    protected void onPostExecute(Long id) {
        if (id == 0) {
            Toast.makeText(activity, "Houve um erro ao salvar o agendamento", Toast.LENGTH_LONG).show();
        } else {
            agendamento.setId(id);
            agendamento.setNovo(false);
            AgendamentoDAO dao = new AgendamentoDAO(activity);
            dao.update(agendamento);
            dao.close();
        }
        progress.dismiss();
        activity.finish();
    }
}
