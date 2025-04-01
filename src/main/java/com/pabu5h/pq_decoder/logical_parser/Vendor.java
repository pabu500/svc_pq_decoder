package com.pabu5h.pq_decoder.logical_parser;

import com.pabu5h.pq_decoder.util.GUID;

public class Vendor {

	public static GUID SATEC = new GUID("e2da5081-7fdb-11d3-9b39-0040052c2d28");

    /// <summary>
    /// The ID for vendor WPT.
    /// </summary>
    public static GUID WPT = new GUID("e2da5082-7fdb-11d3-9b39-0040052c2d28");

    /// <summary>
    /// The ID representing no vendor.
    /// </summary>
    public static GUID None = new GUID("e6b51701-f747-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for vendor BMI.
    /// </summary>
    public static GUID BMI = new GUID("e6b51702-f747-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for vendor BPA.
    /// </summary>
    public static GUID BPA = new GUID("e6b51702-f747-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for vendor CESI.
    /// </summary>
    public static GUID CESI = new GUID("e6b51704-f747-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for vendor Cooper.
    /// </summary>
    public static GUID Cooper = new GUID("e6b51705-f747-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for vendor DCG.
    /// </summary>
    public static GUID DCG = new GUID("e6b51706-f747-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for vendor Dranetz.
    /// </summary>
    public static GUID Dranetz = new GUID("e6b51707-f747-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for vendor EDF.
    /// </summary>
    public static GUID EDF = new GUID("e6b51708-f747-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for vendor Electric Power Research Institute.
    /// </summary>
    public static GUID EPRI = new GUID("e6b51709-f747-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for vendor Electrotek.
    /// </summary>
    public static GUID Electrotek = new GUID("e6b5170a-f747-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for vendor Fluke.
    /// </summary>
    public static GUID Fluke = new GUID("e6b5170b-f747-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for vendor Hydro-Quebec.
    /// </summary>
    public static GUID HydroQuebec = new GUID("e6b5170c-f747-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for vendor IEEE.
    /// </summary>
    public static GUID IEEE = new GUID("e6b5170d-f747-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for vendor Kreiss Johnson.
    /// </summary>
    public static GUID KreissJohnson = new GUID("e6b5170e-f747-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for vendor Metrosonic.
    /// </summary>
    public static GUID Metrosonic = new GUID("e6b5170f-f747-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for vendor PML.
    /// </summary>
    public static GUID PML = new GUID("e6b51710-f747-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for vendor PSI.
    /// </summary>
    public static GUID PSI = new GUID("e6b51711-f747-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for vendor PTI.
    /// </summary>
    public static GUID PTI = new GUID("e6b51712-f747-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for public domain hardware.
    /// </summary>
    public static GUID PublicDomain = new GUID("e6b51713-f747-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for vendor RPM.
    /// </summary>
    public static GUID RPM = new GUID("e6b51714-f747-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for vendor Square D PowerLogic.
    /// </summary>
    public static GUID SquareDPowerLogic = new GUID("e6b51715-f747-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for vendor Telog.
    /// </summary>
    public static GUID Telog = new GUID("e6b51716-f747-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// The ID for vendor PMI.
    /// </summary>
    public static GUID PMI = new GUID("609acec0-993d-11d4-a4b3-444553540000");

    /// <summary>
    /// The ID for vendor Met One.
    /// </summary>
    public static GUID MetOne = new GUID("b5b5da61-e2e1-11d4-82d9-00e09872a094");

    /// <summary>
    /// The ID for vendor Trinergi.
    /// </summary>
    public static GUID Trinergi = new GUID("0fd5a3a8-d73a-11d2-ac3e-444553540000");

    /// <summary>
    /// The ID for vendor General Electric.
    /// </summary>
    public static GUID GE = new GUID("5202bd00-245c-11d5-a4b3-444553540000");

    /// <summary>
    /// The ID for vendor LEM.
    /// </summary>
    public static GUID LEM = new GUID("80c4a722-2816-11d4-8ab4-004005698d26");

    /// <summary>
    /// The ID for vendor ACTL.
    /// </summary>
    public static GUID ACTL = new GUID("80c4a761-2816-11d4-8ab4-004005698d26");

    /// <summary>
    /// The ID for vendor AdvanTech.
    /// </summary>
    public static GUID AdvanTech = new GUID("650f988f-378c-47b8-baed-cccb3f959ad7");

    /// <summary>
    /// The ID for vendor ELCOM.
    /// </summary>
    public static GUID ELCOM = new GUID("f7e9eb70-6f1d-11d6-9cb3-0020e010453b");

    /// <summary>
    /// Gets information about the vendor identified by the given ID.
    /// </summary>
    /// <param name="vendorID">Globally unique identifier for the vendor.</param>
    /// <returns>The information about the vendor.</returns>
//	public static Identifier GetInfo(Guid vendorID)
//    {
//        Identifier identifier;
//        return VendorLookup.TryGetValue(vendorID, out identifier) ? identifier : null;
//    }

    /// <summary>
    /// Converts the given vendor ID to a string containing the name of the vendor.
    /// </summary>
    /// <param name="vendorID">The ID of the vendor to be converted to a string.</param>
    /// <returns>A string containing the name of the vendor with the given ID.</returns>
//    public static string ToString(Guid vendorID)
//    {
//        return GetInfo(vendorID)?.Name ?? vendorID.ToString();
//    }
//
//    private static Dictionary<Guid, Identifier> VendorLookup
//    {
//        get
//        {
//            Tag vendorTag = Tag.GetTag(DataSourceRecord.VendorIDTag);
//
//            if (s_vendorTag != vendorTag)
//            {
//                s_vendorLookup = vendorTag.ValidIdentifiers.ToDictionary(id => Guid.Parse(id.Value));
//                s_vendorTag = vendorTag;
//            }
//
//            return s_vendorLookup;
//        }
//    }
//
//    private static Tag s_vendorTag;
//    private static Dictionary<Guid, Identifier> s_vendorLookup;
}
