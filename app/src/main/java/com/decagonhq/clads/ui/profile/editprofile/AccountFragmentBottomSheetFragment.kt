package com.decagonhq.clads.ui.profile.editprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.decagonhq.clads.R
import com.decagonhq.clads.databinding.AccountFragmentAddAddressBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.widget.RadioButton








class AccountFragmentBottomSheetFragment : BottomSheetDialogFragment() {

    private val _binding: AccountFragmentAddAddressBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.account_fragment_add_address_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle)

//        binding.accountFragmentLocationBottomSheetSetLocationLaterRadioGroup.setOnCheckedChangeListener { group, checkedId ->
//            when(checkedId){
//                R.id.account_fragment_location_bottom_sheet_set_location_later_radio_button -> {
//                    Toast.makeText(requireContext(), "Set location later", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//        }

        val radioButton: RadioButton = binding.accountFragmentLocationBottomSheetSetLocationLaterRadioButton
        radioButton.setOnClickListener {
            if (!radioButton.isSelected) {
                radioButton.isChecked = true
                radioButton.isSelected = true
            } else {
                radioButton.isChecked = false
                radioButton.isSelected = false
            }
        }

    }
}
