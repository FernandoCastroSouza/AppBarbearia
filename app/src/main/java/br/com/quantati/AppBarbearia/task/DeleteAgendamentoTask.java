package br.com.quantati.AppBarbearia.task;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import br.com.quantati.AppBarbearia.MainActivity;
import br.com.quantati.AppBarbearia.dao.AgendamentoDAO;
import br.com.quantati.AppBarbearia.model.Agendamento;
import br.com.quantati.AppBarbearia.ws.WebRequest;


/**
 * Created by Fernando on 12/06/2017.
 */
public class DeleteAgendamentoTask extends AsyncTask<String, Object, Boolean> {
    private static final String QTDE = "qtde";
    private final MainActivity activity;
    private final Agendamento agendamento;
    private ProgressDialog progress;

    public DeleteAgendamentoTask(MainActivity activity, Agendamento agendamento) {
        this.activity = activity;
        this.agendamento = agendamento;
    }

    @Override
    protected void onPreExecute() {
        progress = ProgressDialog.show(activity, "Aguarde...", "Enviando dados!!!", true);
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            WebRequest request = new WebRequest();
            String jsonResult = request.delete(agendamento.getId());
            JSONObject jsonObject = new JSONObject(jsonResult);
            return jsonObject.getLong(QTDE) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean statusOK) {
        if (!statusOK) {
            Toast.makeText(activity, "Houve um erro ao remover o agendamento", Toast.LENGTH_LONG).show();
        } else {
            AgendamentoDAO dao = new AgendamentoDAO(activity);
            dao.delete(agendamento.getId());
            dao.close();
        }
        activity.carregaLista();
        progress.dismiss();
    }
}
