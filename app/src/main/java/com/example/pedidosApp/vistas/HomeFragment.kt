package com.example.pedidosApp.vistas


import android.content.Context
import android.os.Bundle
import android.service.carrier.CarrierMessagingService.ResultCallback
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pedidosApp.MainActivity
import com.example.pedidosApp.R
import com.example.pedidosApp.adapters.ProductoAdapter
import com.example.pedidosApp.config.Config
import com.example.pedidosApp.modelos.Producto
import org.json.JSONArray
import org.json.JSONException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var productos = ArrayList<Producto>()
    private var recyclerView:RecyclerView? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view:View =  inflater.inflate(R.layout.fragment_home, container, false)

         recyclerView = view.findViewById<RecyclerView>(R.id.listaPrincipal)


        recyclerView?.layoutManager = LinearLayoutManager(view.context,LinearLayoutManager.VERTICAL, false)

        /*val linearLayoutManager:LinearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView?.layoutManager = linearLayoutManager*/
        recargar(view.context)


        return view


    }




    fun recargar( contexto:Context){

        var config = Config()
        var url ="${config.ipServer}/productos"
        var jsonObjectRequest = JsonArrayRequest(
            Request.Method.GET,url,null,
            {respuesta:JSONArray->
                for (i in 0 until respuesta.length()){

                    val  item = respuesta.getJSONObject(i)
                    productos.add(Producto(
                        item.getString("id"),
                        item.getString("nombre"),
                        item.getString("precio"),
                        config.image+item.getString("imagen"),
                        item.getString("descripcion"),
                        item.getString("id_categories")

                        ))

                }
                var adapter = ProductoAdapter(productos)
                recyclerView?.adapter = adapter

            }, {

                Log.d("API", "that didn't work")

                });

        var queue = Volley.newRequestQueue(contexto)
        queue.add(jsonObjectRequest)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}