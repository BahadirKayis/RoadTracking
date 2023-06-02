package com.example.roadtracking.presentation.home


import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.roadtracking.R
import com.example.roadtracking.common.SwipeToDeleteCallback
import com.example.roadtracking.common.extensions.collectIn
import com.example.roadtracking.common.extensions.longToDateConvert
import com.example.roadtracking.common.extensions.snackBar
import com.example.roadtracking.data.model.RoadUI
import com.example.roadtracking.databinding.FragmentHomeBinding
import com.example.roadtracking.delegation.viewBinding
import com.example.roadtracking.presentation.MonthPickerDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private lateinit var viewModel: HomeVM
    private lateinit var list: List<RoadUI>
    private val adapter by lazy { RoadAdapter() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[HomeVM::class.java]
        initUIEvent()
        initUIEffect()
        initUIState()
        binding.acTextCompany.clearFocus()
        binding.acTextCompany.doOnTextChanged { text, _, _, _ ->
            viewModel.setEvent(HomeUIEvent.SearchCompany(text.toString()))
        }

    }

    private fun initUIEvent() {
        with(viewModel) {
            binding.etDate.setOnClickListener {
                setEvent(HomeUIEvent.ShowDatePicker)
            }
            binding.buttonSave.setOnClickListener {
                setEvent(
                    HomeUIEvent.SaveData(
                        binding.etDate.text.toString(),
                        binding.acTextCompany.text.toString()
                    )
                )
                binding.etDate.text.clear()
                binding.acTextCompany.text.clear()
            }

            binding.buttonSend.setOnClickListener {

                setEvent(HomeUIEvent.SelectMonth)
            }

            val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val task = adapter.currentList[viewHolder.layoutPosition]
                    viewModel.setEvent(HomeUIEvent.DeleteRoadItem(task))
                }
            }
            val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(binding.rcRoad)
        }
    }

    private fun initUIEffect() = viewModel.effect.collectIn(viewLifecycleOwner) { effect ->
        when (effect) {
            is HomeUIEffect.ShowDatePicker -> {
                findNavController().navigate(R.id.action_homeFragment_to_datePickerFragment)
            }

            is HomeUIEffect.ShowToast -> requireView().snackBar(effect.message)

            is HomeUIEffect.SendMonth -> {
                toPdf(effect.month)

            }

            HomeUIEffect.ShowMonthPicker -> {
                val dialogFragment = MonthPickerDialogFragment()
                dialogFragment.show(requireActivity().supportFragmentManager, "MonthPickerDialog")
            }
        }
    }


    private fun initUIState() = viewModel.state.collectIn(viewLifecycleOwner) { state ->
        with(state) {
            date?.let {
                binding.etDate.setText(it)
            }
            roadUI?.let {
                list = it
                adapter.submitList(it)
                binding.rcRoad.adapter = adapter
            }
            companyList?.let {
                autoCompleteAdapter(it)
            }
        }

    }

    private fun autoCompleteAdapter(company: List<String>) {
        val adapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_ch, company)
        binding.acTextCompany.setAdapter(adapter)
        binding.acTextCompany.threshold = 1
    }

    private fun toPdf(road: List<RoadUI>) {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        val formattedMonth = month.toString().padStart(2, '0')
        val pdfName = "Kazım Kayış-$formattedMonth-$year.pdf"

        val pageInfo = PdfDocument.PageInfo.Builder(500, 1000, 1).create()
        val pdfDocument = PdfDocument()

        val paint = Paint().apply {
            textSize = 40f // Başlık için kullanılacak yazı boyutu
            isFakeBoldText = true // Kalın yazı stili
            textAlign = Paint.Align.CENTER // Başlığın hizalama şekli
        }


        val fontMetrics = paint.fontMetrics
        val lineHeight = fontMetrics.descent - fontMetrics.ascent

        val startVerticalPosition = 100f // İçeriğin 100 piksel aşağıdan başlamasını sağlar
        val x = pageInfo.pageWidth / 2f // Metnin ortalanacağı yatay pozisyon
        var y =
            startVerticalPosition // İlk dikey pozisyon, başlangıç dikey pozisyonu olarak belirlenir

        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        // Başlığı PDF sayfasına çizin
        val title = "Yol Masrafı"
        canvas.drawText(title, x, y, paint)

        paint.apply { textSize = 16f;isFakeBoldText = false;textAlign = Paint.Align.LEFT }
        y += lineHeight

        val columnX = 50f // Sütunların sol kenarından başlayacak şekilde yatay pozisyon

        for (item in road) {
            val date = item.dateInMillis.longToDateConvert()
            val company = item.company

            canvas.drawText("$date - $company", columnX, y, paint)

            // Yeni bir satıra geçmek için y koordinatını güncelleyin
            y += lineHeight
        }
        pdfDocument.finishPage(page)

        val file = File(context?.getExternalFilesDir(null), pdfName)
        pdfDocument.writeTo(FileOutputStream(file))
        pdfDocument.close()

        // Paylaşım işlemi için uygun bir Intent oluşturun
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "application/pdf"
        shareIntent.putExtra(
            Intent.EXTRA_STREAM,
            FileProvider.getUriForFile(requireContext(), "com.bahadir.RoadTracking", file)
        )
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        // Diğer uygulamalarla paylaşın
        context?.startActivity(Intent.createChooser(shareIntent, "PDF Paylaş"))
    }

}
