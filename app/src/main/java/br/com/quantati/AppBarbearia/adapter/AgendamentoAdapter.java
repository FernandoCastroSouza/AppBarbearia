package br.com.quantati.AppBarbearia.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import br.com.quantati.AppBarbearia.model.Agendamento;
import br.com.quantati.AppBarbearia.util.ImageUtil;
import br.com.quantati.barbearia.R;


/**
 * Created by Fernando on 12/06/2017.
 */

public class AgendamentoAdapter extends BaseAdapter {

    private Activity activity;
    private List<Agendamento> lista;

    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private TextView txtNomeCliente, txtTelefoneCliente, txtDataHora;
    private ImageView fotoCliente;

    public AgendamentoAdapter(Activity activity, List<Agendamento> lista) {
        this.activity = activity;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Agendamento getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lista.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View layout = activity.getLayoutInflater().inflate(R.layout.agendamento, parent, false);
        fotoCliente = (ImageView) layout.findViewById(R.id.itemFoto);
        txtNomeCliente = (TextView) layout.findViewById(R.id.txtNomeCliente);
        txtTelefoneCliente = (TextView) layout.findViewById(R.id.txtTelefoneCliente);
        txtDataHora = (TextView) layout.findViewById(R.id.txtDataHoraAgendamento);

        Agendamento agendamento = getItem(position);
        txtNomeCliente.setText(agendamento.getNome());
        txtTelefoneCliente.setText(agendamento.getTelefone());
        txtDataHora.setText(format.format(agendamento.getDataHora().getTime()));

        Bitmap foto;
        if (!agendamento.getFotoAntes().isEmpty()) {
            foto = BitmapFactory.decodeFile(agendamento.getFotoAntes());
        } else {
            foto = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_no_image);
        }
        try {
            fotoCliente.setImageBitmap(ImageUtil.crocpCircle(foto));
        } catch (NullPointerException e) {
            System.out.println("Agendamento sem foto " + e.getMessage());
        }
        return layout;
    }
}
