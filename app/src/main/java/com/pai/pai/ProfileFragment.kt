package com.pai.pai
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.pai.pai.models.UserObject

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    private var context2: Context? = null
    override fun onAttach(context: Context){
        super.onAttach(context)
        this.context2 = context
    }

    companion object{
        private  const val ARG_OBJECT = "object"
    }
    //VAMOS A IMPRIMIR CADA UNO:

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.activity_profile, container, false)
        val view = inflater.inflate(R.layout.activity_profile, container, false)

        val txtName = view.findViewById<EditText>(R.id.input_email_P)
        val txtEmail = view.findViewById<EditText>(R.id.input_username_P)
        val txtPass = view.findViewById<EditText>(R.id.input_contraseña_P)

        txtName.setText(UserObject.getName())
        txtEmail.setText(UserObject.getEmail())
        txtPass.setText(UserObject.getPass())

        val check = view.findViewById<CheckBox>(R.id.checkBox)
        val btnAceptar = view.findViewById<Button>(R.id.btn_edit_P)

        btnAceptar.setOnClickListener{
            if(check.isChecked){
                Toast.makeText(context2, "Se encriptarán los mensajes", Toast.LENGTH_SHORT).show()
                UserObject.setEncript(true)
            }
            else{
                Toast.makeText(context2, "Sus mensajes no serán encriptados", Toast.LENGTH_SHORT).show()
                UserObject.setEncript(false)
            }
            val intent = Intent(context2, DrawerActivity::class.java)
            startActivity(intent)
        }

        return view

    }

    //Va apesar el argumento del adaptador
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        //Esto es un get si contiene el argumento tal
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            //val textView: TextView = view.findViewById(R.id.txtTitle)
            //textView.text = "Fragment: " + getInt(ARG_OBJECT).toString()



        }
    }




}