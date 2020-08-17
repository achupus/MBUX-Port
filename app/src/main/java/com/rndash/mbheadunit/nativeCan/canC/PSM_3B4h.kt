
@file:Suppress("unused", "FunctionName")
package com.rndash.mbheadunit.nativeCan.canC
import com.rndash.mbheadunit.CanFrame // AUTO GEN
import com.rndash.mbheadunit.nativeCan.CanBusNative // AUTO GEN

/**
 *   Generated by db_converter.py
 *   Object for PSM_3B4h (ID 0x03B4)
**/

object PSM_3B4h {

    	/** Gets Working speed control active **/
	fun get_psm_adr_akt() : Boolean = CanBusNative.getECUParameterC(CanCAddrs.PSM_3B4h, 2, 1) != 0
	
	/** Sets Working speed control active **/
	fun set_psm_adr_akt(f: CanFrame, p: Boolean) = CanBusNative.setFrameParameter(f, 2, 1, if(p) 1 else 0)
	
	/** Gets working speed control **/
	fun get_psm_adr_tgl() : Boolean = CanBusNative.getECUParameterC(CanCAddrs.PSM_3B4h, 1, 1) != 0
	
	/** Sets working speed control **/
	fun set_psm_adr_tgl(f: CanFrame, p: Boolean) = CanBusNative.setFrameParameter(f, 1, 1, if(p) 1 else 0)
	
	/** Gets working speed control **/
	fun get_psm_adr_par() : Boolean = CanBusNative.getECUParameterC(CanCAddrs.PSM_3B4h, 0, 1) != 0
	
	/** Sets working speed control **/
	fun set_psm_adr_par(f: CanFrame, p: Boolean) = CanBusNative.setFrameParameter(f, 0, 1, if(p) 1 else 0)
	
	/** Gets Motor target speed ADR **/
	fun get_psm_n_soll() : Int = CanBusNative.getECUParameterC(CanCAddrs.PSM_3B4h, 8, 16)
	
	/** Sets Motor target speed ADR **/
	fun set_psm_n_soll(f: CanFrame, p: Int) = CanBusNative.setFrameParameter(f, 8, 16, p)
	
	/** Gets torque limitation active **/
	fun get_psm_mom_akt() : Boolean = CanBusNative.getECUParameterC(CanCAddrs.PSM_3B4h, 26, 1) != 0
	
	/** Sets torque limitation active **/
	fun set_psm_mom_akt(f: CanFrame, p: Boolean) = CanBusNative.setFrameParameter(f, 26, 1, if(p) 1 else 0)
	
	/** Gets torque limit **/
	fun get_psm_mom_tgl() : Boolean = CanBusNative.getECUParameterC(CanCAddrs.PSM_3B4h, 25, 1) != 0
	
	/** Sets torque limit **/
	fun set_psm_mom_tgl(f: CanFrame, p: Boolean) = CanBusNative.setFrameParameter(f, 25, 1, if(p) 1 else 0)
	
	/** Gets torque limitation **/
	fun get_psm_mom_par() : Boolean = CanBusNative.getECUParameterC(CanCAddrs.PSM_3B4h, 24, 1) != 0
	
	/** Sets torque limitation **/
	fun set_psm_mom_par(f: CanFrame, p: Boolean) = CanBusNative.setFrameParameter(f, 24, 1, if(p) 1 else 0)
	
	/** Gets Maximum motor torque **/
	fun get_psm_mom_soll() : Int = CanBusNative.getECUParameterC(CanCAddrs.PSM_3B4h, 27, 13)
	
	/** Sets Maximum motor torque **/
	fun set_psm_mom_soll(f: CanFrame, p: Int) = CanBusNative.setFrameParameter(f, 27, 13, p)
	
	/** Gets speed limitation active **/
	fun get_psm_dz_akt() : Boolean = CanBusNative.getECUParameterC(CanCAddrs.PSM_3B4h, 42, 1) != 0
	
	/** Sets speed limitation active **/
	fun set_psm_dz_akt(f: CanFrame, p: Boolean) = CanBusNative.setFrameParameter(f, 42, 1, if(p) 1 else 0)
	
	/** Gets speed limitation **/
	fun get_psm_dz_tgl() : Boolean = CanBusNative.getECUParameterC(CanCAddrs.PSM_3B4h, 41, 1) != 0
	
	/** Sets speed limitation **/
	fun set_psm_dz_tgl(f: CanFrame, p: Boolean) = CanBusNative.setFrameParameter(f, 41, 1, if(p) 1 else 0)
	
	/** Gets speed limitation **/
	fun get_psm_dz_par() : Boolean = CanBusNative.getECUParameterC(CanCAddrs.PSM_3B4h, 40, 1) != 0
	
	/** Sets speed limitation **/
	fun set_psm_dz_par(f: CanFrame, p: Boolean) = CanBusNative.setFrameParameter(f, 40, 1, if(p) 1 else 0)
	
	/** Gets maximum speed **/
	fun get_psm_dz_max() : Int = CanBusNative.getECUParameterC(CanCAddrs.PSM_3B4h, 48, 16)
	
	/** Sets maximum speed **/
	fun set_psm_dz_max(f: CanFrame, p: Int) = CanBusNative.setFrameParameter(f, 48, 16, p)
	
	
}