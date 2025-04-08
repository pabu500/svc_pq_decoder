package com.pabu5h.pq_decoder.logical_parser;

import com.pabu5h.pq_decoder.util.GUID;

public class SeriesValueType {
	/// <summary>
    /// Value type for a measurement.
    /// </summary>
    public static GUID Val = new GUID("67f6af97-f753-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Time.
    /// </summary>
    public static GUID Time = new GUID("c690e862-f755-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Minimum.
    /// </summary>
    public static GUID Min = new GUID("67f6af98-f753-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Maximum.
    /// </summary>
    public static GUID Max = new GUID("67f6af99-f753-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Average.
    /// </summary>
    public static GUID Avg = new GUID("67f6af9a-f753-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Instantaneous.
    /// </summary>
    public static GUID Inst = new GUID("67f6af9b-f753-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Phase angle.
    /// </summary>
    public static GUID PhaseAngle = new GUID("3d786f9d-f76e-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Phase angle which corresponds to a <see cref="Min"/> series.
    /// </summary>
    public static GUID PhaseAngleMin = new GUID("dc762340-3c56-11d2-ae44-0060083a2628");

    /// <summary>
    /// Phase angle which corresponds to a <see cref="Max"/> series.
    /// </summary>
    public static GUID PhaseAngleMax = new GUID("dc762341-3c56-11d2-ae44-0060083a2628");

    /// <summary>
    /// Phase angle which corresponds to an <see cref="Avg"/> series.
    /// </summary>
    public static GUID PhaseAngleAvg = new GUID("dc762342-3c56-11d2-ae44-0060083a2628");

    /// <summary>
    /// Area under the signal, usually an rms voltage, current, or other quantity.
    /// </summary>
    public static GUID Area = new GUID("c7825ce0-8ace-11d3-b92f-0050da2b1f4d");

    /// <summary>
    /// Latitude.
    /// </summary>
    public static GUID Latitude = new GUID("c690e864-f755-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Duration.
    /// </summary>
    public static GUID Duration = new GUID("c690e863-f755-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Longitude.
    /// </summary>
    public static GUID Longitude = new GUID("c690e865-f755-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Polarity.
    /// </summary>
    public static GUID Polarity = new GUID("c690e866-f755-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Ellipse (for lightning flash density).
    /// </summary>
    public static GUID Ellipse = new GUID("c690e867-f755-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// BinID.
    /// </summary>
    public static GUID BinID = new GUID("c690e869-f755-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// BinHigh.
    /// </summary>
    public static GUID BinHigh = new GUID("c690e86a-f755-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// BinLow.
    /// </summary>
    public static GUID BinLow = new GUID("c690e86b-f755-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// XBinHigh.
    /// </summary>
    public static GUID XBinHigh = new GUID("c690e86c-f755-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// XBinLow.
    /// </summary>
    public static GUID XBinLow = new GUID("c690e86d-f755-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// YBinHigh.
    /// </summary>
    public static GUID YBinHigh = new GUID("c690e86e-f755-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// YBinLow.
    /// </summary>
    public static GUID YBinLow = new GUID("c690e86f-f755-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Count.
    /// </summary>
    public static GUID Count = new GUID("c690e870-f755-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Transition event code series.
    /// </summary>
    /// <remarks>
    /// This series contains codes corresponding to values in a value
    /// series that indicates what kind of transition caused the event
    /// to be recorded. Used only with VALUELOG data.
    /// </remarks>
    public static GUID Transition = new GUID("5369c260-c347-11d2-923f-00104b2b84b1");

    /// <summary>
    /// Cumulative probability in percent.
    /// </summary>
    public static GUID Prob = new GUID("6763cc71-17d6-11d4-9f1c-002078e0b723");

    /// <summary>
    /// Interval data.
    /// </summary>
    public static GUID Interval = new GUID("72e82a40-336c-11d5-a4b3-444553540000");

    /// <summary>
    /// Status data.
    /// </summary>
    public static GUID Status = new GUID("b82b5c82-55c7-11d5-a4b3-444553540000");

    /// <summary>
    /// Probability: 1%.
    /// </summary>
    public static GUID P1 = new GUID("67f6af9c-f753-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Probability: 5%.
    /// </summary>
    public static GUID P5 = new GUID("67f6af9d-f753-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Probability: 10%.
    /// </summary>
    public static GUID P10 = new GUID("67f6af9e-f753-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Probability: 90%.
    /// </summary>
    public static GUID P90 = new GUID("67f6af9f-f753-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Probability: 95%.
    /// </summary>
    public static GUID P95 = new GUID("c690e860-f755-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Probability: 99%.
    /// </summary>
    public static GUID P99 = new GUID("c690e861-f755-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Frequency.
    /// </summary>
    public static GUID Frequency = new GUID("c690e868-f755-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Gets information about the series value type identified by the given ID.
    /// </summary>
    /// <param name="seriesValueTypeID">The identifier for the series value type.</param>
    /// <returns>Information about the series value type.</returns>
//    public static Identifier GetInfo(Guid seriesValueTypeID)
//    {
//        Identifier identifier;
//        return SeriesValueTypeLookup.TryGetValue(seriesValueTypeID, out identifier) ? identifier : null;
//    }

    /// <summary>
    /// Returns the name of the given series value type.
    /// </summary>
    /// <param name="seriesValueTypeID">The GUID tag which identifies the series value type.</param>
    /// <returns>The name of the given series value type.</returns>
//    public static string ToString(Guid seriesValueTypeID)
//    {
//        return GetInfo(seriesValueTypeID)?.Name;
//    }

//    private static Dictionary<Guid, Identifier> SeriesValueTypeLookup
//    {
//        get
//        {
//            Tag seriesValueTypeTag = Tag.GetTag(SeriesDefinition.ValueTypeIDTag);
//
//            if (s_seriesValueTypeTag != seriesValueTypeTag)
//            {
//                s_seriesValueTypeLookup = seriesValueTypeTag.ValidIdentifiers.ToDictionary(id => GUID.Parse(id.Value));
//                s_seriesValueTypeTag = seriesValueTypeTag;
//            }
//
//            return s_seriesValueTypeLookup;
//        }
//    }
//
//    private static Tag s_seriesValueTypeTag;
//    private static Dictionary<Guid, Identifier> s_seriesValueTypeLookup;
}
