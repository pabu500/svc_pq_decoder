package com.pabu5h.pq_decoder.logical_parser;

import com.pabu5h.pq_decoder.util.GUID;

public class QuantityType {
    /// <summary>
    /// Point-on-wave measurements.
    /// </summary>
    public static GUID WaveForm = new GUID("67f6af80-f753-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Time-based logged entries.
    /// </summary>
    public static GUID ValueLog = new GUID("67f6af82-f753-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Time-domain measurements including
    /// magnitudes and (optionally) phase angle.
    /// </summary>
    public static GUID Phasor = new GUID("67f6af81-f753-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Frequency-domain measurements including
    /// magnitude and (optionally) phase angle.
    /// </summary>
    public static GUID Response = new GUID("67f6af85-f753-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Time, latitude, longitude, value, polarity, ellipse.
    /// </summary>
    public static GUID Flash = new GUID("67f6af83-f753-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// BinLow, BinHigh, BinID, count.
    /// </summary>
    public static GUID Histogram = new GUID("67f6af87-f753-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// XBinLow, XBinHigh, YBinLow, YBinHigh, BinID, count.
    /// </summary>
    public static GUID Histogram3D = new GUID("67f6af88-f753-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Probability, value.
    /// </summary>
    public static GUID CPF = new GUID("67f6af89-f753-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// X-values and y-values.
    /// </summary>
    public static GUID XY = new GUID("67f6af8a-f753-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Magnitude and duration.
    /// </summary>
    public static GUID MagDur = new GUID("67f6af8b-f753-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// X-values, y-values, and z-values.
    /// </summary>
    public static GUID XYZ = new GUID("67f6af8c-f753-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Time, magnitude, and duration.
    /// </summary>
    public static GUID MagDurTime = new GUID("67f6af8d-f753-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Time, magnitude, duration, and count.
    /// </summary>
    public static GUID MagDurCount = new GUID("67f6af8e-f753-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Gets the information about the quantity
    /// type identified by the given ID.
    /// </summary>
    /// <param name="quantityTypeID">The quantity type ID.</param>
    /// <returns>Information about the quantity type.</returns>
//    public static Identifier GetInfo(Guid quantityTypeID)
//    {
//        Identifier identifier;
//        return QuantityTypeLookup.TryGetValue(quantityTypeID, out identifier) ? identifier : null;
//    }
//
//    /// <summary>
//    /// Gets the name of the quantity type with the given ID.
//    /// </summary>
//    /// <param name="quantityTypeID">The ID of the quantity type.</param>
//    /// <returns>The name of the quantity type with the given ID.</returns>
//    public static string ToString(Guid quantityTypeID)
//    {
//        return GetInfo(quantityTypeID)?.Name;
//    }
//
//    /// <summary>
//    /// Determines whether the given ID is a quantity type ID.
//    /// </summary>
//    /// <param name="id">The ID to be tested.</param>
//    /// <returns>True if the given ID is a quantity type ID; false otherwise.</returns>
//    public static bool IsQuantityTypeID(Guid id)
//    {
//        return (object)GetInfo(id) != null;
//    }
//
//    private static Dictionary<Guid, Identifier> QuantityTypeLookup
//    {
//        get
//        {
//            Tag quantityTypeTag = Tag.GetTag(ChannelDefinition.QuantityTypeIDTag);
//
//            if (s_quantityTypeTag != quantityTypeTag)
//            {
//                s_quantityTypeLookup = quantityTypeTag.ValidIdentifiers.ToDictionary(id => GUID.Parse(id.Value));
//                s_quantityTypeTag = quantityTypeTag;
//            }
//
//            return s_quantityTypeLookup;
//        }
//    }
//
//    private static Tag s_quantityTypeTag;
//    private static Dictionary<Guid, Identifier> s_quantityTypeLookup;
}
