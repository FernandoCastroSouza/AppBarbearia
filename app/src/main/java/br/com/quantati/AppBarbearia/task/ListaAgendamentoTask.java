package br.com.quantati.AppBarbearia.task;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import br.com.quantati.AppBarbearia.MainActivity;
import br.com.quantati.AppBarbearia.converter.AgendamentoConverter;
import br.com.quantati.AppBarbearia.dao.AgendamentoDAO;
import br.com.quantati.AppBarbearia.model.Agendamento;
import br.com.quantati.AppBarbearia.util.ImageUtil;
import br.com.quantati.AppBarbearia.ws.WebRequest;


/**
 * Created by Fernando on 12/06/2017.
 */
public class ListaAgendamentoTask extends AsyncTask<String, Object, Boolean> {
    private final MainActivity activity;
    private ProgressDialog progress;

    public ListaAgendamentoTask(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        progress = ProgressDialog.show(activity, "Aguarde...", "Obtendo dados!!!", true);
    }

    private Bitmap downloadImageBitmap(String url) {
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            WebRequest request = new WebRequest();
            String jsonResult = request.list();
            JSONArray jsonArray = new JSONArray(jsonResult);
            List<Agendamento> agendamentos = new AgendamentoConverter().fromJson(jsonArray);
            if (agendamentos != null && !agendamentos.isEmpty()) {
                AgendamentoDAO dao = new AgendamentoDAO(activity);
                FileOutputStream fos;
                Bitmap image;
                for (Agendamento agendamento : agendamentos) {
                    if (agendamento.getFotoAntes() != null) {
                        agendamento.setFotoAntes(activity.getExternalFilesDir(null) + "/" + agendamento.getFotoAntes());
                    }
                    if (dao.findById(String.valueOf(agendamento.getId())) == null) {
                        agendamento.setImporting(true);
                        if (agendamento.getFotoAntes() != null && !agendamento.getFotoAntes().equals("")) {
                            ImageUtil.saveImage(agendamento.getFotoAntes(), agendamento.getFotoAntes());
                        }
                        if (agendamento.getFotoDepois() != null && !agendamento.getFotoDepois().equals("")) {
                            ImageUtil.saveImage(agendamento.getFotoDepois(), agendamento.getFotoDepois());
                        }
                        dao.insert(agendamento);
                    } else {
                        dao.update(agendamento);
                    }
                }
                dao.close();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean statusOK) {
        if (!statusOK) {
            Toast.makeText(activity, "Houve um erro ao obter a lista de agendamentos", Toast.LENGTH_LONG).show();
        } else {
            activity.carregaLista();
        }
        progress.dismiss();
    }
}
