package com.example.soal_uts_bug_fixing

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import androidx.wear.compose.materialcore.is24HourFormat
import com.example.soal_uts_bug_fixing.databinding.ActivityAppointmentBinding
import com.example.soal_uts_bug_fixing.databinding.ActivityFormBinding
import android.text.format.DateFormat
import android.view.View
import android.widget.RadioButton
import com.example.soal_uts_bug_fixing.databinding.DialogExitBinding
import java.util.Calendar

class AppointmentActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, DialogExit.DialogListener
{
    private lateinit var binding: ActivityAppointmentBinding

    companion object{
        const val EXTRA_TELEFON = "extra_phone"
        const val EXTRA_ALAMAT = "extra_alamat"
        const val EXTRA_TIPE = "extra_tipe"
        const val EXTRA_TANGGAL = "extra_tanggal"
        const val EXTRA_WAKTU = "extra_waktu"
    }

    private lateinit var dateInput : String
    private lateinit var timeInput : String
    private lateinit var tipePertemuan : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dateInput = ""
        timeInput = ""
        tipePertemuan =""

        with(binding){

            tombolKalender.setOnClickListener {
                val datePicker = DatePicker()
                datePicker.show(supportFragmentManager, "datePicker")
            }

            tombolJam.setOnClickListener {
                val timePicker = TimePicker()
                timePicker.show(supportFragmentManager, "timePicker")
            }



            tombolSubmit.setOnClickListener {
                if(fieldNotEmpty()){
                    val dialog = DialogExit()
                    //
                }else{
                    Toast.makeText(this@AppointmentActivity, "MASIH ADA KOLOM YANG KOSONG", Toast.LENGTH_SHORT).show()
                }
            }


            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.radioButton1 -> {
                        tipePertemuan = radioButton1.text.toString()
                    }
                    R.id.radioButton2 -> {
                        tipePertemuan = radioButton2.text.toString()
                    }
                }
                if(tipePertemuan=="Online"){
                    inputLayout.visibility = View.GONE
                }else{
                    inputLayout.visibility = View.VISIBLE
                }
            }

        }

    }


    override fun onDateSet(p0: android.widget.DatePicker?, day: Int, month: Int, year:
    Int) {
        //
    }

    override fun onTimeSet(p0: android.widget.TimePicker?, hour: Int, menit:Int) {
        timeInput = String.format("%02d:%02d", hour, menit)  // Menggunakan menit, bukan minute
        binding.timerTxt.text = timeInput
    }


//  AKSI SETELAH KONFIRMASI DIALOG BOX
    override fun onDialogResult(result: Boolean) {
        val nama = intent.getStringExtra(FormActivity.EXTRA_NAMA)
        val identitas = intent.getStringExtra(FormActivity.EXTRA_IDENTITAS)
        val gender = intent.getStringExtra(FormActivity.EXTRA_GENDER)

            if (result) {
                val intentToResult = Intent(this@AppointmentActivity, ResultActivity::class.java)
                intentToResult.putExtra(EXTRA_TELEFON, binding.kontakEdt.text.toString())
                intentToResult.putExtra(EXTRA_TANGGAL, binding.kalenderTxt.text.toString())


                intentToResult.putExtra(EXTRA_WAKTU, binding.timerTxt.text.toString()) //menambahkan intentToResult.putExtra
                intentToResult.putExtra(EXTRA_TIPE, tipePertemuan) //menambahkan intentToResult.putExtra

                intentToResult.putExtra(FormActivity.EXTRA_NAMA, nama)
                intentToResult.putExtra(FormActivity.EXTRA_IDENTITAS, identitas)
                intentToResult.putExtra(FormActivity.EXTRA_GENDER, gender)

                if(tipePertemuan=="Offline"){
                    intentToResult.putExtra(EXTRA_ALAMAT, binding.lokasiEdt.text.toString())
                }
                startActivity(intentToResult)
            }
    }

    fun fieldNotEmpty(): Boolean {
        with(binding){
            if(kontakEdt.text.toString()!="" && tipePertemuan!="" && timeInput!="" && dateInput!=""){
                if(tipePertemuan=="Offline"){
                    if(lokasiEdt.text.toString()!=""){
                        return true
                    }else{
                        return false
                    }
                }else{
                    return true
                }
            }else{
                return false
            }
        }
    }

}

class DatePicker: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        //
        //
        //
        return DatePickerDialog(
            requireActivity(),
            activity as DatePickerDialog.OnDateSetListener,
            year,
            monthOfYear,
            dayOfMonth
        )
    }
}

class TimePicker: DialogFragment() {
//
}

class DialogExit : DialogFragment() {

    interface DialogListener {
        abstract val tombolKalender: Any

        fun onDialogResult(result: Boolean)
    }
    private lateinit var listener: DialogListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as DialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement DialogListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val binding = DialogExitBinding.inflate(inflater)

        with(binding) {
            yesBtn.setOnClickListener {
                listener.onDialogResult(true)
                dismiss()
            }
            noBtn.setOnClickListener {
                listener.onDialogResult(false)
                dismiss()
            }
        }
        builder.setView(binding.root)
        return builder.create()
    }
}
