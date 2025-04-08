package com.pabu5h.pq_decoder.logical_parser;

import com.pabu5h.pq_decoder.util.GUID;

public class DisturbanceCategory {
	/// <summary>
    /// The ID for no distrubance or undefined.
    /// </summary>
    public static GUID None = new GUID("67f6af8f-f753-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for a IEEE 1159 Transient.
    /// </summary>
    public static GUID Transient = new GUID("67f6af90-f753-11cf-9d89-0080c72e70a3");//new GUID("67f6af90-f753-0x11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for a IEEE 1159 Impulsive Transient.
    /// </summary>
    public static GUID ImpulsiveTransient = new GUID("dd56ef60-7edd-11d2-b30a-00609789d193");

    /// <summary>
    /// The ID for a IEEE 1159 Impulsive Transient with nanosecond duration.
    /// </summary>
    public static GUID ImpulsiveTransient_nano = new GUID("dd56ef61-7edd-11d2-b30a-00609789d193");

    /// <summary>
    /// The ID for a IEEE 1159 Impulsive Transient with microsecond duration.
    /// </summary>
    public static GUID ImpulsiveTransient_micro = new GUID("dd56ef63-7edd-11d2-b30a-00609789d193");

    /// <summary>
    /// The ID for a IEEE 1159 Impulsive Transient with milisecond duration.
    /// </summary>

    public static GUID ImpulsiveTransient_mili = new GUID("dd56ef64-7edd-11d2-b30a-00609789d193");

    /// <summary>
    /// The ID for a IEEE 1159 Oscillatory Transient.
    /// </summary>
    public static GUID OscillatoryTransient = new GUID("dd56ef65-7edd-11d2-b30a-00609789d193");

    /// <summary>
    /// The ID for a IEEE 1159 Low Frequency Oscillatory Transient.
    /// </summary>
    public static GUID OscillatoryTransient_low = new GUID("dd56ef66-7edd-11d2-b30a-00609789d193");

    /// <summary>
    /// The ID for a IEEE 1159 Medium Frequency Oscillatory Transient.
    /// </summary>
    public static GUID OscillatoryTransient_medium = new GUID("dd56ef67-7edd-11d2-b30a-00609789d193");

    /// <summary>
    /// The ID for a IEEE 1159 High Frequency Oscillatory Transient.
    /// </summary>
    public static GUID OscillatoryTransient_high = new GUID("dd56ef68-7edd-11d2-b30a-00609789d193");

    /// <summary>
    /// The ID for a IEEE 1159 Short Duration RMS Variation
    /// </summary>n
    public static GUID RMSVariationShortDuration  = new GUID("67f6af91-f753-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for a IEEE 1159 Short Duration RMS Variation - Instantaneous duration.
    /// </summary>
    public static GUID RMSVariationShortDuration_Instantaneous = new GUID("dd56ef69-7edd-11d2-b30a-00609789d193");

    /// <summary>
    /// The ID for a IEEE 1159 Short Duration RMS Variation - Instantaneous Sag.
    /// </summary>
    public static GUID RMSVariationShortDuration_InstantaneousSag = new GUID("dd56ef6a-7edd-11d2-b30a-00609789d193");

    /// <summary>
    /// The ID for a IEEE 1159 Short Duration RMS Variation - Instantaneous Swell.
    /// </summary>
    public static GUID RMSVariationShortDuration_InstantaneousSwell = new GUID("dd56ef6b-7edd-11d2-b30a-00609789d193");

    /// <summary>
    /// The ID for a IEEE 1159 Short Duration RMS Variation - Momentary Duration.
    /// </summary>
    public static GUID RMSVariationShortDuration_Momentary = new GUID("dd56ef6c-7edd-11d2-b30a-00609789d193");

    /// <summary>
    /// The ID for a IEEE 1159 Short Duration RMS Variation - Momentary Interruption.
    /// </summary>
    public static GUID RMSVariationShortDuration_MomentaryInterruption = new GUID("dd56ef6d-7edd-11d2-b30a-00609789d193");

    /// <summary>
    /// The ID for a IEEE 1159 Short Duration RMS Variation - Momentary Sag.
    /// </summary>
    public static GUID RMSVariationShortDuration_MomentarySag = new GUID("dd56ef6e-7edd-11d2-b30a-00609789d193");

    /// <summary>
    /// The ID for a IEEE 1159 Short Duration RMS Variation - Momentary Swell.
    /// </summary>
    public static GUID RMSVariationShortDuration_MomentarySwell = new GUID("dd56ef6f-7edd-11d2-b30a-00609789d193");

    /// <summary>
    /// The ID for a IEEE 1159Short Duration RMS Variation - Temporary Duration.
    /// </summary>
    public static GUID RMSVariationShortDuration_Temporary = new GUID("dd56ef70-7edd-11d2-b30a-00609789d193");

    /// <summary>
    /// The ID for a IEEE 1159 Short Duration RMS Variation - Temporary Interruption.
    /// </summary>
    public static GUID RMSVariationShortDuration_TemporaryInterruption = new GUID("dd56ef71-7edd-11d2-b30a-00609789d193");

    /// <summary>
    /// The ID for a IEEE 1159 Short Duration RMS Variation - Temporary Sag.
    /// </summary>
    public static GUID RMSVariationShortDuration_TemporarySag = new GUID("dd56ef72-7edd-11d2-b30a-00609789d193");

    /// <summary>
    /// The ID for a IEEE 1159 Short Duration RMS Variation - Temporary Swell.
    /// </summary>
    public static GUID RMSVariationShortDuration_TemporarySwell = new GUID("dd56ef73-7edd-11d2-b30a-00609789d193");

    /// <summary>
    /// The ID for a IEEE 1159  Long Duration RMS Variation.
    /// </summary>
    public static GUID RMSVariationLongDuration = new GUID("67f6af92-f753-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for a IEEE 1159 Long Duration RMS Variation - Interruption.
    /// </summary>
    public static GUID RMSVariationLongDuration_Interrruption = new GUID("dd56ef74-7edd-11d2-b30a-00609789d193");

    /// <summary>
    /// The ID for a IEEE 1159 Long Duration RMS Variation - Undervoltage.
    /// </summary>
    public static GUID RMSVariationLongDuration_UnderVoltage = new GUID("dd56ef75-7edd-11d2-b30a-00609789d193");

    /// <summary>
    /// The ID for a IEEE 1159 Long Duration RMS Variation - Overvoltage.
    /// </summary>
    public static GUID RMSVariationLongDuration_OverVoltage = new GUID("dd56ef76-7edd-11d2-b30a-00609789d193");

    /// <summary>
    /// The ID for a IEEE 1159 Imbalance.
    /// </summary>
    public static GUID Imbalance = new GUID("dd56ef77-7edd-11d2-b30a-00609789d193");

    /// <summary>
    /// The ID for a IEEE 1159 Power Frequency Variation.
    /// </summary>
    public static GUID PowerFrequencyVariation = new GUID("dd56ef7e-7edd-11d2-b30a-00609789d193");

    /// <summary>
    /// The ID for a IEEE 1159 Voltage Fluctuation.
    /// </summary>
    public static GUID VoltageFuctuation = new GUID("67f6af93-f753-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for a IEEE 1159 Waveform Distortion.
    /// </summary>
    public static GUID WaveformDistortion = new GUID("67f6af94-f753-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for a IEEE 1159 DC offset of voltage or current waveform.
    /// </summary>
    public static GUID DCoffset = new GUID("dd56ef78-7edd-11d2-b30a-00609789d193");

    /// <summary>
    /// The ID for a IEEE 1159 Waveform Harmonics Present.
    /// </summary>
    public static GUID WaveformHarmonics = new GUID("dd56ef79-7edd-11d2-b30a-00609789d193");

    /// <summary>
    /// The ID for a IEEE 1159 Waveform Interharmonics Present.
    /// </summary>

    public static GUID WaveformInterHarmonics = new GUID("dd56ef7a-7edd-11d2-b30a-00609789d193");
    /// <summary>
    /// The ID for a IEEE 1159 Waveform Notching Present.
    /// </summary>
    public static GUID WaveformNotching = new GUID("67f6af95-f753-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for a IEEE 1159 Waveform Noise Present.
    /// </summary>
    public static GUID WaveformNoise = new GUID("67f6af96-f753-11cf-9d89-0080c72e70a3");



    /// <summary>
    /// Gets information about the Disturbance identified by the given ID.
    /// </summary>
    /// <param name="disturbanceCategoryID">Globally unique identifier for the Disturbance Category.</param>
    /// <returns>The information about the vendor.</returns>
//    public static Identifier GetInfo(GUID disturbanceCategoryID)
//    {
//        Identifier identifier;
//        return DisturbanceLookup.TryGetValue(disturbanceCategoryID, out identifier) ? identifier : null;
//    }
//
//    /// <summary>
//    /// Converts the given Disturbance ID to a string containing the name of the Disturbance.
//    /// </summary>
//    /// <param name="disturbanceCategoryID">The ID of the Disturbance to be converted to a string.</param>
//    /// <returns>A string containing the name of the Disturbance Category with the given ID.</returns>
//    public static string ToString(GUID disturbanceCategoryID)
//    {
//        return GetInfo(disturbanceCategoryID)?.Name ?? disturbanceCategoryID.ToString();
//    }
//
//    private static Dictionary<GUID, Identifier> DisturbanceLookup
//    {
//        get
//        {
//            Tag disturbanceTag = Tag.GetTag(ObservationRecord.DisturbanceCategoryTag);
//
//            if (s_disturbanceTag != disturbanceTag)
//            {
//                s_disturbanceLookup = disturbanceTag.ValidIdentifiers.ToDictionary(id => GUID.Parse(id.Value));
//                s_disturbanceTag = disturbanceTag;
//            }
//
//            return s_disturbanceLookup;
//        }
//    }
//
//    private static Tag s_disturbanceTag;
//    private static Dictionary<GUID, Identifier> s_disturbanceLookup;
}
