package com.pabu5h.pq_decoder.logical_parser;

import com.pabu5h.pq_decoder.util.GUID;

public class QuantityCharacteristic {
    /// <summary>
    /// No quantity characteristic.
    /// </summary>
    public static GUID None = new GUID("a6b31adf-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// Instantaneous f(t).
    /// </summary>
    public static GUID Instantaneous = new GUID("a6b31add-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// Spectra F(F).
    /// </summary>
    public static GUID Spectra = new GUID("a6b31ae9-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// Peak value.
    /// </summary>
    public static GUID Peak = new GUID("a6b31ae2-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// RMS value.
    /// </summary>
    public static GUID RMS = new GUID("a6b31ae5-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// Harmonic RMS.
    /// </summary>
    public static GUID HRMS = new GUID("a6b31adc-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// Frequency.
    /// </summary>
    public static GUID Frequency = new GUID("07ef68af-9ff5-11d2-b30b-006008b37183");

    /// <summary>
    /// Total harmonic distortion (%).
    /// </summary>
    public static GUID TotalTHD = new GUID("a6b31aec-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// Even harmonic distortion (%).
    /// </summary>
    public static GUID EvenTHD = new GUID("a6b31ad4-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// Odd harmonic distortion (%).
    /// </summary>
    public static GUID OddTHD = new GUID("a6b31ae0-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// Crest factor.
    /// </summary>
    public static GUID CrestFactor = new GUID("a6b31ad2-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// Form factor.
    /// </summary>
    public static GUID FormFactor = new GUID("a6b31adb-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// Arithmetic sum.
    /// </summary>
    public static GUID ArithSum = new GUID("a6b31ad0-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// Zero sequence component unbalance (%).
    /// </summary>
    public static GUID S0S1 = new GUID("a6b31ae7-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// Negative sequence component unbalance (%).
    /// </summary>
    public static GUID S2S1 = new GUID("a6b31ae8-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// Positive sequence component.
    /// </summary>
    public static GUID SPos = new GUID("a6b31aea-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// Negative sequence component.
    /// </summary>
    public static GUID SNeg = new GUID("d71a4b91-3c92-11d4-9f2c-002078e0b723");

    /// <summary>
    /// Zero sequence component.
    /// </summary>
    public static GUID SZero = new GUID("d71a4b92-3c92-11d4-9f2c-002078e0b723");

    /// <summary>
    /// Imbalance by max deviation from average.
    /// </summary>
    public static GUID AvgImbal = new GUID("a6b31ad1-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// Total THD normalized to RMS.
    /// </summary>
    public static GUID TotalTHDRMS = new GUID("f3d216e0-2aa5-11d5-a4b3-444553540000");

    /// <summary>
    /// Odd THD normalized to RMS.
    /// </summary>
    public static GUID OddTHDRMS = new GUID("f3d216e1-2aa5-11d5-a4b3-444553540000");

    /// <summary>
    /// Even THD normalized to RMS.
    /// </summary>
    public static GUID EvenTHDRMS = new GUID("f3d216e2-2aa5-11d5-a4b3-444553540000");

    /// <summary>
    /// Total interharmonic distortion.
    /// </summary>
    public static GUID TID = new GUID("f3d216e3-2aa5-11d5-a4b3-444553540000");

    /// <summary>
    /// Total interharmonic distortion normalized to RMS.
    /// </summary>
    public static GUID TIDRMS = new GUID("f3d216e4-2aa5-11d5-a4b3-444553540000");

    /// <summary>
    /// Interharmonic RMS.
    /// </summary>
    public static GUID IHRMS = new GUID("f3d216e5-2aa5-11d5-a4b3-444553540000");

    /// <summary>
    /// Spectra by harmonic group index.
    /// </summary>
    public static GUID SpectraHGroup = new GUID("53be6ba8-0789-455b-9a95-da128683dda7");

    /// <summary>
    /// Spectra by interharmonic group index.
    /// </summary>
    public static GUID SpectraIGroup = new GUID("5e51e006-9c95-4c5e-878f-7ca87c0d2a0e");

    /// <summary>
    /// TIF.
    /// </summary>
    public static GUID TIF = new GUID("a6b31aeb-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// Flicker average RMS value.
    /// </summary>
    public static GUID FlkrMagAvg = new GUID("a6b31ad6-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// dV/V base.
    /// </summary>
    public static GUID FlkrMaxDVV = new GUID("a6b31ad8-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// Frequence of maximum flicker harmonic.
    /// </summary>
    public static GUID FlkrFreqMax = new GUID("a6b31ad5-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// Magnitude of maximum flicker harmonic.
    /// </summary>
    public static GUID FlkrMagMax = new GUID("a6b31ad7-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// Spectrum weighted average.
    /// </summary>
    public static GUID FlkrWgtAvg = new GUID("a6b31ada-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// Flicker spectrum VRMS(F).
    /// </summary>
    public static GUID FlkrSpectrum = new GUID("a6b31ad9-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// Short term flicker.
    /// </summary>
    public static GUID FlkrPST = new GUID("515bf320-71ca-11d4-a4b3-444553540000");

    /// <summary>
    /// Long term flicker.
    /// </summary>
    public static GUID FlkrPLT = new GUID("515bf321-71ca-11d4-a4b3-444553540000");

    /// <summary>
    /// TIF normalized to RMS.
    /// </summary>
    public static GUID TIFRMS = new GUID("f3d216e6-2aa5-11d5-a4b3-444553540000");

    /// <summary>
    /// Sliding PLT.
    /// </summary>
    public static GUID FlkrPLTSlide = new GUID("2257ec05-06ea-4709-b43a-0c00534d554a");

    /// <summary>
    /// Pi LPF.
    /// </summary>
    public static GUID FlkrPiLPF = new GUID("4d693eec-5d1d-4531-993a-793b5356c63d");

    /// <summary>
    /// Pi max.
    /// </summary>
    public static GUID FlkrPiMax = new GUID("126de61c-6691-4d16-8fdf-46482bca4694");

    /// <summary>
    /// Pi root.
    /// </summary>
    public static GUID FlkrPiRoot = new GUID("e065b621-ffdb-4598-9330-4d09353988b6");

    /// <summary>
    /// Pi root LPF.
    /// </summary>
    public static GUID FlkrPiRootLPF = new GUID("7d11f283-1ce7-4e58-8af0-79048793b8a7");

    /// <summary>
    /// IT.
    /// </summary>
    public static GUID IT = new GUID("a6b31ade-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// RMS value of current for a demand interval.
    /// </summary>
    public static GUID RMSDemand = new GUID("07ef68a0-9ff5-11d2-b30b-006008b37183");

    /// <summary>
    /// Transformer derating factor.
    /// </summary>
    public static GUID ANSITDF = new GUID("8786ca10-9113-11d3-b930-0050da2b1f4d");

    /// <summary>
    /// Transformer K factor.
    /// </summary>
    public static GUID KFactor = new GUID("8786ca11-9113-11d3-b930-0050da2b1f4d");

    /// <summary>
    /// Total demand distortion.
    /// </summary>
    public static GUID TDD = new GUID("f3d216e7-2aa5-11d5-a4b3-444553540000");

    /// <summary>
    /// Peak demand current.
    /// </summary>
    public static GUID RMSPeakDemand = new GUID("72e82a44-336c-11d5-a4b3-444553540000");

    /// <summary>
    /// Real power (watts).
    /// </summary>
    public static GUID P = new GUID("a6b31ae1-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// Reactive power (VAR).
    /// </summary>
    public static GUID Q = new GUID("a6b31ae4-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// Apparent power (VA).
    /// </summary>
    public static GUID S = new GUID("a6b31ae6-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// True power factor - (Vrms * Irms) / P.
    /// </summary>
    public static GUID PF = new GUID("a6b31ae3-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// Displacement factor - Cosine of the phase angle between fundamental frequency voltage and current phasors.
    /// </summary>
    public static GUID DF = new GUID("a6b31ad3-b451-11d1-ae17-0060083a2628");

    /// <summary>
    /// Value of active power for a demand interval.
    /// </summary>
    public static GUID PDemand = new GUID("07ef68a1-9ff5-11d2-b30b-006008b37183");

    /// <summary>
    /// Value of reactive power for a demand interval.
    /// </summary>
    public static GUID QDemand = new GUID("07ef68a2-9ff5-11d2-b30b-006008b37183");

    /// <summary>
    /// Value of apparent power for a demand interval.
    /// </summary>
    public static GUID SDemand = new GUID("07ef68a3-9ff5-11d2-b30b-006008b37183");

    /// <summary>
    /// Value of displacement power factor for a demand interval.
    /// </summary>
    public static GUID DFDemand = new GUID("07ef68a4-9ff5-11d2-b30b-006008b37183");

    /// <summary>
    /// Value of true power factor for a demand interval.
    /// </summary>
    public static GUID PFDemand = new GUID("07ef68a5-9ff5-11d2-b30b-006008b37183");

    /// <summary>
    /// Predicted value of active power for current demand interval.
    /// </summary>
    public static GUID PPredDemand = new GUID("672d0305-7810-11d4-a4b3-444553540000");

    /// <summary>
    /// Predicted value of reactive power for current demand interval.
    /// </summary>
    public static GUID QPredDemand = new GUID("672d0306-7810-11d4-a4b3-444553540000");

    /// <summary>
    /// Predicted value of apparent power for current demand interval.
    /// </summary>
    public static GUID SPredDemand = new GUID("672d0307-7810-11d4-a4b3-444553540000");

    /// <summary>
    /// Value of active power coincident with reactive power demand.
    /// </summary>
    public static GUID PCoQDemand = new GUID("672d030a-7810-11d4-a4b3-444553540000");

    /// <summary>
    /// Value of active power coincident with apparent power demand.
    /// </summary>
    public static GUID PCoSDemand = new GUID("672d030b-7810-11d4-a4b3-444553540000");

    /// <summary>
    /// Value of reactive power coincident with active power demand.
    /// </summary>
    public static GUID QCoPDemand = new GUID("672d030d-7810-11d4-a4b3-444553540000");

    /// <summary>
    /// Value of reactive power coincident with apparent power demand.
    /// </summary>
    public static GUID QCoSDemand = new GUID("672d030e-7810-11d4-a4b3-444553540000");

    /// <summary>
    /// Value of displacement power factor coincident with apparent power demand.
    /// </summary>
    public static GUID DFCoSDemand = new GUID("07ef68ad-9ff5-11d2-b30b-006008b37183");

    /// <summary>
    /// Value of true power factor coincident with apparent power demand.
    /// </summary>
    public static GUID PFCoSDemand = new GUID("07ef68ae-9ff5-11d2-b30b-006008b37183");

    /// <summary>
    /// Value of true power factor coincident with active power demand.
    /// </summary>
    public static GUID PFCoPDemand = new GUID("672d0308-7810-11d4-a4b3-444553540000");

    /// <summary>
    /// Value of true power factor coincident with reactive power demand.
    /// </summary>
    public static GUID PFCoQDemand = new GUID("672d0309-7810-11d4-a4b3-444553540000");

    /// <summary>
    /// Value of the power angle at fundamental frequency. 
    /// </summary>
    public static GUID AngleFund = new GUID("672d030f-7810-11d4-a4b3-444553540000");

    /// <summary>
    /// Value of the reactive power at fundamental frequency.
    /// </summary>
    public static GUID QFund = new GUID("672d0310-7810-11d4-a4b3-444553540000");

    /// <summary>
    /// True power factor - IEEE vector calculations.
    /// </summary>
    public static GUID PFVector = new GUID("672d0311-7810-11d4-a4b3-444553540000");

    /// <summary>
    /// Displacement factor - IEEE vector calculations.
    /// </summary>
    public static GUID DFVector = new GUID("672d0312-7810-11d4-a4b3-444553540000");

    /// <summary>
    /// Value of apparent power - IEEE vector calculations.
    /// </summary>
    public static GUID SVector = new GUID("672d0314-7810-11d4-a4b3-444553540000");

    /// <summary>
    /// Value of fundamental frequency apparent power - IEEE vector calculations.
    /// </summary>
    public static GUID SVectorFund = new GUID("672d0315-7810-11d4-a4b3-444553540000");

    /// <summary>
    /// Value of fundamental frequency apparent power.
    /// </summary>
    public static GUID SFund = new GUID("672d0316-7810-11d4-a4b3-444553540000");

    /// <summary>
    /// Apparent power coincident with active power demand.
    /// </summary>
    public static GUID SCoPDemand = new GUID("672d0317-7810-11d4-a4b3-444553540000");

    /// <summary>
    /// Apparent power coincident with reactive power demand.
    /// </summary>
    public static GUID SCoQDemand = new GUID("672d0318-7810-11d4-a4b3-444553540000");

    /// <summary>
    /// True power factor - IEEE arithmetic calculations.
    /// </summary>
    public static GUID PFArith = new GUID("1c39fb00-a6aa-11d4-a4b3-444553540000");

    /// <summary>
    /// Displacement factor - IEEE arithmetic calculations.
    /// </summary>
    public static GUID DFArith = new GUID("1c39fb01-a6aa-11d4-a4b3-444553540000");

    /// <summary>
    /// Value of apparent power - IEEE arithmetic calculations.
    /// </summary>
    public static GUID SArith = new GUID("1c39fb02-a6aa-11d4-a4b3-444553540000");

    /// <summary>
    /// Value of fundamental frequency apparent power - IEEE arithmetic calculations.
    /// </summary>
    public static GUID SArithFund = new GUID("1c39fb03-a6aa-11d4-a4b3-444553540000");

    /// <summary>
    /// Peak apparent power demand.
    /// </summary>
    public static GUID SPeakDemand = new GUID("72e82a43-336c-11d5-a4b3-444553540000");

    /// <summary>
    /// Peak reactive power demand.
    /// </summary>
    public static GUID QPeakDemand = new GUID("72e82a42-336c-11d5-a4b3-444553540000");

    /// <summary>
    /// Peak active power demand.
    /// </summary>
    public static GUID PPeakDemand = new GUID("72e82a41-336c-11d5-a4b3-444553540000");

    /// <summary>
    /// Net harmonic active power.
    /// </summary>
    public static GUID PHarmonic = new GUID("b82b5c80-55c7-11d5-a4b3-444553540000");

    /// <summary>
    /// Arithmetic sum harmonic active power.
    /// </summary>
    public static GUID PHarmonicUnsigned = new GUID("b82b5c81-55c7-11d5-a4b3-444553540000");

    /// <summary>
    /// Value of fundamental frequency real power.
    /// </summary>
    public static GUID PFund = new GUID("1cdda475-1ebb-42d8-8087-d01b0b5cfa97");

    /// <summary>
    /// Value of active power integrated over time (Energy - watt-hours).
    /// </summary>
    public static GUID PIntg = new GUID("07ef68a6-9ff5-11d2-b30b-006008b37183");

    /// <summary>
    /// Value of active power integrated over time (Energy - watt-hours) in the positive direction (toward load).
    /// </summary>
    public static GUID PIntgPos = new GUID("07ef68a7-9ff5-11d2-b30b-006008b37183");

    /// <summary>
    /// Value of active fundamental frequency power integrated over time
    /// (Energy - watt-hours) in the positive direction (toward load).
    /// </summary>
    public static GUID PIntgPosFund = new GUID("672d0300-7810-11d4-a4b3-444553540000");

    /// <summary>
    /// Value of active power integrated over time (Energy - watt-hours) in the negative direction (away from load).
    /// </summary>
    public static GUID PIntgNeg = new GUID("07ef68a8-9ff5-11d2-b30b-006008b37183");

    /// <summary>
    /// Value of active fundamental frequency power integrated over time
    /// (Energy - watt-hours) in the negative direction (away from load).
    /// </summary>
    public static GUID PIntgNegFund = new GUID("672d0301-7810-11d4-a4b3-444553540000");

    /// <summary>
    /// Value of reactive power integrated over time (var-hours).
    /// </summary>
    public static GUID QIntg = new GUID("07ef68a9-9ff5-11d2-b30b-006008b37183");

    /// <summary>
    /// Value of reactive power integrated over time (Energy - watt-hours) in the positive direction (toward load).
    /// </summary>
    public static GUID QIntgPos = new GUID("07ef68aa-9ff5-11d2-b30b-006008b37183");

    /// <summary>
    /// Value of fundamental frequency reactive power integrated over time
    /// (Energy - watt-hours) in the positive direction (toward load).
    /// </summary>
    public static GUID QIntgPosFund = new GUID("672d0303-7810-11d4-a4b3-444553540000");

    /// <summary>
    /// Value of fundamental frequency reactive power integrated over time
    /// (Energy - watt-hours) in the negative direction (away from load).
    /// </summary>
    public static GUID QIntgNegFund = new GUID("672d0304-7810-11d4-a4b3-444553540000");

    /// <summary>
    /// Value of reactive power integrated over time (Energy - watt-hours) in the negative direction (away from load).
    /// </summary>
    public static GUID QIntgNeg = new GUID("07ef68ab-9ff5-11d2-b30b-006008b37183");

    /// <summary>
    /// Value of apparent power integrated over time (VA-hours).
    /// </summary>
    public static GUID SIntg = new GUID("07ef68ac-9ff5-11d2-b30b-006008b37183");

    /// <summary>
    /// Value of fundamental frequency apparent power integrated over time (VA-hours).
    /// </summary>
    public static GUID SIntgFund = new GUID("672d0313-7810-11d4-a4b3-444553540000");

    /// <summary>
    /// Value of active power integrated over time (Energy - watt-hours).
    /// </summary>
    public static GUID PIVLIntg = new GUID("f098a9a0-3ee4-11d5-a4b3-444553540000");

    /// <summary>
    /// Value of active power integrated over time (Energy - watt-hours) in the positive direction (toward load).
    /// </summary>
    public static GUID PIVLIntgPos = new GUID("f098a9a1-3ee4-11d5-a4b3-444553540000");

    /// <summary>
    /// Value of active fundamental frequency power integrated over time
    /// (Energy - watt-hours) in the positive direction (toward load).
    /// </summary>
    public static GUID PIVLIntgPosFund = new GUID("f098a9a2-3ee4-11d5-a4b3-444553540000");

    /// <summary>
    /// Value of active power integrated over time (Energy - watt-hours) in the negative direction (away from load).
    /// </summary>
    public static GUID PIVLIntgNeg = new GUID("f098a9a3-3ee4-11d5-a4b3-444553540000");

    /// <summary>
    /// Value of active fundamental frequency power integrated over time
    /// (Energy - watt-hours) in the negative direction (away from load).
    /// </summary>
    public static GUID PIVLIntgNegFund = new GUID("f098a9a4-3ee4-11d5-a4b3-444553540000");

    /// <summary>
    /// Value of reactive power integrated over time (var-hours).
    /// </summary>
    public static GUID QIVLIntg = new GUID("f098a9a5-3ee4-11d5-a4b3-444553540000");

    /// <summary>
    /// Value of reactive power integrated over time (Energy - watt-hours) in the positive direction (toward load).
    /// </summary>
    public static GUID QIVLIntgPos = new GUID("f098a9a6-3ee4-11d5-a4b3-444553540000");

    /// <summary>
    /// Value of fundamental frequency reactive power integrated over time
    /// (Energy - watt-hours) in the positive direction (toward load).
    /// </summary>
    public static GUID QIVLIntgPosFund = new GUID("f098a9a7-3ee4-11d5-a4b3-444553540000");

    /// <summary>
    /// Value of fundamental frequency reactive power integrated over time
    /// (Energy - watt-hours) in the negative direction (away from load).
    /// </summary>
    public static GUID QIVLIntgNegFund = new GUID("f098a9a8-3ee4-11d5-a4b3-444553540000");

    /// <summary>
    /// Value of reactive power integrated over time (Energy - watt-hours) in the negative direction (away from load).
    /// </summary>
    public static GUID QIVLIntgNeg = new GUID("f098a9a9-3ee4-11d5-a4b3-444553540000");

    /// <summary>
    /// Value of apparent power integrated over time (VA-hours).
    /// </summary>
    public static GUID SIVLIntg = new GUID("f098a9aa-3ee4-11d5-a4b3-444553540000");

    /// <summary>
    /// Value of fundamental frequency apparent power integrated over time (VA-hours).
    /// </summary>
    public static GUID SIVLIntgFund = new GUID("f098a9ab-3ee4-11d5-a4b3-444553540000");

    /// <summary>
    /// D axis components.
    /// </summary>
    public static GUID DAxisField = new GUID("d347ba65-e34c-11d4-82d9-00e09872a094");

    /// <summary>
    /// Q axis components.
    /// </summary>
    public static GUID QAxis = new GUID("d347ba64-e34c-11d4-82d9-00e09872a094");

    /// <summary>
    /// Rotational position.
    /// </summary>
    public static GUID Rotational = new GUID("d347ba62-e34c-11d4-82d9-00e09872a094");

    /// <summary>
    /// D axis components.
    /// </summary>
    public static GUID DAxis = new GUID("d347ba63-e34c-11d4-82d9-00e09872a094");

    /// <summary>
    /// Linear position.
    /// </summary>
    public static GUID Linear = new GUID("d347ba61-e34c-11d4-82d9-00e09872a094");

    /// <summary>
    /// Transfer function.
    /// </summary>
    public static GUID TransferFunc = new GUID("5202bd07-245c-11d5-a4b3-444553540000");

    /// <summary>
    /// Status data.
    /// </summary>
    public static GUID Status = new GUID("b82b5c83-55c7-11d5-a4b3-444553540000");

    /// <summary>
    /// Gets information about the quantity
    /// characteristic identified by the given ID.
    /// </summary>
    /// <param name="quantityCharacteristicID">The identifier for the quantity characteristic.</param>
    /// <returns>Inforamtion about the quantity characteristic.</returns>
//    public static Identifier GetInfo(GUID quantityCharacteristicID)
//    {
//        Identifier identifier;
//        return QuantityCharacteristicLookup.TryGetValue(quantityCharacteristicID, out identifier) ? identifier : null;
//    }
//
//    /// <summary>
//    /// Returns the name of the given quantity characteristic.
//    /// </summary>
//    /// <param name="quantityCharacteristicID">The GUID tag which identifies the quantity characteristic.</param>
//    /// <returns>The name of the given quantity characteristic.</returns>
//    public static string ToName(GUID quantityCharacteristicID)
//    {
//        return GetInfo(quantityCharacteristicID)?.Name;
//    }
//
//    /// <summary>
//    /// Returns a string representation of the given quantity characteristic.
//    /// </summary>
//    /// <param name="quantityCharacteristicID">The GUID tag which identifies the quantity characteristic.</param>
//    /// <returns>The name of the given quantity characteristic.</returns>
//    public static string ToString(GUID quantityCharacteristicID)
//    {
//        return GetInfo(quantityCharacteristicID)?.Description;
//    }
//
//    private static Dictionary<GUID, Identifier> QuantityCharacteristicLookup
//    {
//        get
//        {
//            Tag quantityCharacteristicTag = Tag.GetTag(SeriesDefinition.QuantityCharacteristicIDTag);
//
//            if (s_quantityCharacteristicTag != quantityCharacteristicTag)
//            {
//                s_quantityCharacteristicLookup = quantityCharacteristicTag.ValidIdentifiers.ToDictionary(id => GUID.Parse(id.Value));
//                s_quantityCharacteristicTag = quantityCharacteristicTag;
//            }
//
//            return s_quantityCharacteristicLookup;
//        }
//    }
//
//    private static Tag s_quantityCharacteristicTag;
//    private static Dictionary<GUID, Identifier> s_quantityCharacteristicLookup;
}
