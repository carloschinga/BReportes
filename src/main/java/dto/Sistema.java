/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author USUARIO
 */
@Entity
@Table(name = "sistema")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sistema.findAll", query = "SELECT s FROM Sistema s"),
    @NamedQuery(name = "Sistema.findAllJSON", query = "SELECT s.siscod,s.sisent,s.sisdlvsta FROM Sistema s where s.estado='S'"),
    @NamedQuery(name = "Sistema.findBySiscod", query = "SELECT s FROM Sistema s WHERE s.siscod = :siscod"),
    @NamedQuery(name = "Sistema.findBySistit", query = "SELECT s FROM Sistema s WHERE s.sistit = :sistit"),
    @NamedQuery(name = "Sistema.findBySisent", query = "SELECT s FROM Sistema s WHERE s.sisent = :sisent"),
    @NamedQuery(name = "Sistema.findBySisdir", query = "SELECT s FROM Sistema s WHERE s.sisdir = :sisdir"),
    @NamedQuery(name = "Sistema.findBySisruc", query = "SELECT s FROM Sistema s WHERE s.sisruc = :sisruc"),
    @NamedQuery(name = "Sistema.findBySisigv", query = "SELECT s FROM Sistema s WHERE s.sisigv = :sisigv"),
    @NamedQuery(name = "Sistema.findBySisenc", query = "SELECT s FROM Sistema s WHERE s.sisenc = :sisenc"),
    @NamedQuery(name = "Sistema.findBySisusr", query = "SELECT s FROM Sistema s WHERE s.sisusr = :sisusr"),
    @NamedQuery(name = "Sistema.findBySistel", query = "SELECT s FROM Sistema s WHERE s.sistel = :sistel"),
    @NamedQuery(name = "Sistema.findBySisver", query = "SELECT s FROM Sistema s WHERE s.sisver = :sisver"),
    @NamedQuery(name = "Sistema.findBySiscolor", query = "SELECT s FROM Sistema s WHERE s.siscolor = :siscolor"),
    @NamedQuery(name = "Sistema.findByEstado", query = "SELECT s FROM Sistema s WHERE s.estado = :estado"),
    @NamedQuery(name = "Sistema.findByFeccre", query = "SELECT s FROM Sistema s WHERE s.feccre = :feccre"),
    @NamedQuery(name = "Sistema.findByFecumv", query = "SELECT s FROM Sistema s WHERE s.fecumv = :fecumv"),
    @NamedQuery(name = "Sistema.findByUsecod", query = "SELECT s FROM Sistema s WHERE s.usecod = :usecod"),
    @NamedQuery(name = "Sistema.findByUsenam", query = "SELECT s FROM Sistema s WHERE s.usenam = :usenam"),
    @NamedQuery(name = "Sistema.findByHostname", query = "SELECT s FROM Sistema s WHERE s.hostname = :hostname"),
    @NamedQuery(name = "Sistema.findBySiscapPac", query = "SELECT s FROM Sistema s WHERE s.siscapPac = :siscapPac"),
    @NamedQuery(name = "Sistema.findBySisalfPac", query = "SELECT s FROM Sistema s WHERE s.sisalfPac = :sisalfPac"),
    @NamedQuery(name = "Sistema.findBySisacfPac", query = "SELECT s FROM Sistema s WHERE s.sisacfPac = :sisacfPac"),
    @NamedQuery(name = "Sistema.findBySistdfPac", query = "SELECT s FROM Sistema s WHERE s.sistdfPac = :sistdfPac"),
    @NamedQuery(name = "Sistema.findBySisdifPac", query = "SELECT s FROM Sistema s WHERE s.sisdifPac = :sisdifPac"),
    @NamedQuery(name = "Sistema.findBySiscamInt", query = "SELECT s FROM Sistema s WHERE s.siscamInt = :siscamInt"),
    @NamedQuery(name = "Sistema.findBySisnlfVen", query = "SELECT s FROM Sistema s WHERE s.sisnlfVen = :sisnlfVen"),
    @NamedQuery(name = "Sistema.findBySistarVen", query = "SELECT s FROM Sistema s WHERE s.sistarVen = :sistarVen"),
    @NamedQuery(name = "Sistema.findBySisertVen", query = "SELECT s FROM Sistema s WHERE s.sisertVen = :sisertVen"),
    @NamedQuery(name = "Sistema.findBySisprtVen", query = "SELECT s FROM Sistema s WHERE s.sisprtVen = :sisprtVen"),
    @NamedQuery(name = "Sistema.findBySislrpVen", query = "SELECT s FROM Sistema s WHERE s.sislrpVen = :sislrpVen"),
    @NamedQuery(name = "Sistema.findBySisdvmVen", query = "SELECT s FROM Sistema s WHERE s.sisdvmVen = :sisdvmVen"),
    @NamedQuery(name = "Sistema.findBySisermVen", query = "SELECT s FROM Sistema s WHERE s.sisermVen = :sisermVen"),
    @NamedQuery(name = "Sistema.findBySisprmVen", query = "SELECT s FROM Sistema s WHERE s.sisprmVen = :sisprmVen"),
    @NamedQuery(name = "Sistema.findBySiserdVen", query = "SELECT s FROM Sistema s WHERE s.siserdVen = :siserdVen"),
    @NamedQuery(name = "Sistema.findBySisprdVen", query = "SELECT s FROM Sistema s WHERE s.sisprdVen = :sisprdVen"),
    @NamedQuery(name = "Sistema.findBySiserhVen", query = "SELECT s FROM Sistema s WHERE s.siserhVen = :siserhVen"),
    @NamedQuery(name = "Sistema.findBySisprhVen", query = "SELECT s FROM Sistema s WHERE s.sisprhVen = :sisprhVen"),
    @NamedQuery(name = "Sistema.findBySisirhVen", query = "SELECT s FROM Sistema s WHERE s.sisirhVen = :sisirhVen"),
    @NamedQuery(name = "Sistema.findBySisfrhVen", query = "SELECT s FROM Sistema s WHERE s.sisfrhVen = :sisfrhVen"),
    @NamedQuery(name = "Sistema.findBySisecuInv", query = "SELECT s FROM Sistema s WHERE s.sisecuInv = :sisecuInv"),
    @NamedQuery(name = "Sistema.findBySistcuInv", query = "SELECT s FROM Sistema s WHERE s.sistcuInv = :sistcuInv"),
    @NamedQuery(name = "Sistema.findBySiscacCli", query = "SELECT s FROM Sistema s WHERE s.siscacCli = :siscacCli"),
    @NamedQuery(name = "Sistema.findBySismrmVen", query = "SELECT s FROM Sistema s WHERE s.sismrmVen = :sismrmVen"),
    @NamedQuery(name = "Sistema.findBySisficVen", query = "SELECT s FROM Sistema s WHERE s.sisficVen = :sisficVen"),
    @NamedQuery(name = "Sistema.findBySismonS", query = "SELECT s FROM Sistema s WHERE s.sismonS = :sismonS"),
    @NamedQuery(name = "Sistema.findBySismonD", query = "SELECT s FROM Sistema s WHERE s.sismonD = :sismonD"),
    @NamedQuery(name = "Sistema.findBySispathUpdate", query = "SELECT s FROM Sistema s WHERE s.sispathUpdate = :sispathUpdate"),
    @NamedQuery(name = "Sistema.findBySisremCom", query = "SELECT s FROM Sistema s WHERE s.sisremCom = :sisremCom"),
    @NamedQuery(name = "Sistema.findBySismdocCaj", query = "SELECT s FROM Sistema s WHERE s.sismdocCaj = :sismdocCaj"),
    @NamedQuery(name = "Sistema.findBySismodprInv", query = "SELECT s FROM Sistema s WHERE s.sismodprInv = :sismodprInv"),
    @NamedQuery(name = "Sistema.findBySisvarprInv", query = "SELECT s FROM Sistema s WHERE s.sisvarprInv = :sisvarprInv"),
    @NamedQuery(name = "Sistema.findBySisdocopCaj", query = "SELECT s FROM Sistema s WHERE s.sisdocopCaj = :sisdocopCaj"),
    @NamedQuery(name = "Sistema.findBySiscotstkVen", query = "SELECT s FROM Sistema s WHERE s.siscotstkVen = :siscotstkVen"),
    @NamedQuery(name = "Sistema.findBySispassfcVen", query = "SELECT s FROM Sistema s WHERE s.sispassfcVen = :sispassfcVen"),
    @NamedQuery(name = "Sistema.findBySispassctVen", query = "SELECT s FROM Sistema s WHERE s.sispassctVen = :sispassctVen"),
    @NamedQuery(name = "Sistema.findBySisreginfCaj", query = "SELECT s FROM Sistema s WHERE s.sisreginfCaj = :sisreginfCaj"),
    @NamedQuery(name = "Sistema.findBySisprescfcVen", query = "SELECT s FROM Sistema s WHERE s.sisprescfcVen = :sisprescfcVen"),
    @NamedQuery(name = "Sistema.findBySisprescctVen", query = "SELECT s FROM Sistema s WHERE s.sisprescctVen = :sisprescctVen"),
    @NamedQuery(name = "Sistema.findBySiscarcliVen", query = "SELECT s FROM Sistema s WHERE s.siscarcliVen = :siscarcliVen"),
    @NamedQuery(name = "Sistema.findBySismrdVen", query = "SELECT s FROM Sistema s WHERE s.sismrdVen = :sismrdVen"),
    @NamedQuery(name = "Sistema.findBySismsmVen", query = "SELECT s FROM Sistema s WHERE s.sismsmVen = :sismsmVen"),
    @NamedQuery(name = "Sistema.findBySisvlo", query = "SELECT s FROM Sistema s WHERE s.sisvlo = :sisvlo"),
    @NamedQuery(name = "Sistema.findBySisstknegVen", query = "SELECT s FROM Sistema s WHERE s.sisstknegVen = :sisstknegVen"),
    @NamedQuery(name = "Sistema.findBySistrdtoInv", query = "SELECT s FROM Sistema s WHERE s.sistrdtoInv = :sistrdtoInv"),
    @NamedQuery(name = "Sistema.findBySisentTip", query = "SELECT s FROM Sistema s WHERE s.sisentTip = :sisentTip"),
    @NamedQuery(name = "Sistema.findBySisdiscoInv", query = "SELECT s FROM Sistema s WHERE s.sisdiscoInv = :sisdiscoInv"),
    @NamedQuery(name = "Sistema.findBySisregdtoInv", query = "SELECT s FROM Sistema s WHERE s.sisregdtoInv = :sisregdtoInv"),
    @NamedQuery(name = "Sistema.findBySisticom", query = "SELECT s FROM Sistema s WHERE s.sisticom = :sisticom"),
    @NamedQuery(name = "Sistema.findBySismail", query = "SELECT s FROM Sistema s WHERE s.sismail = :sismail"),
    @NamedQuery(name = "Sistema.findBySisnewctVen", query = "SELECT s FROM Sistema s WHERE s.sisnewctVen = :sisnewctVen"),
    @NamedQuery(name = "Sistema.findBySisnewfcVen", query = "SELECT s FROM Sistema s WHERE s.sisnewfcVen = :sisnewfcVen"),
    @NamedQuery(name = "Sistema.findBySiswsecVen", query = "SELECT s FROM Sistema s WHERE s.siswsecVen = :siswsecVen"),
    @NamedQuery(name = "Sistema.findBySisanuvtaCaj", query = "SELECT s FROM Sistema s WHERE s.sisanuvtaCaj = :sisanuvtaCaj"),
    @NamedQuery(name = "Sistema.findBySisconimpCaj", query = "SELECT s FROM Sistema s WHERE s.sisconimpCaj = :sisconimpCaj"),
    @NamedQuery(name = "Sistema.findBySisvalrucCaj", query = "SELECT s FROM Sistema s WHERE s.sisvalrucCaj = :sisvalrucCaj"),
    @NamedQuery(name = "Sistema.findBySistidvalCaj", query = "SELECT s FROM Sistema s WHERE s.sistidvalCaj = :sistidvalCaj"),
    @NamedQuery(name = "Sistema.findBySisdirf", query = "SELECT s FROM Sistema s WHERE s.sisdirf = :sisdirf"),
    @NamedQuery(name = "Sistema.findBySismsg1", query = "SELECT s FROM Sistema s WHERE s.sismsg1 = :sismsg1"),
    @NamedQuery(name = "Sistema.findBySismsg2", query = "SELECT s FROM Sistema s WHERE s.sismsg2 = :sismsg2"),
    @NamedQuery(name = "Sistema.findBySisalmtrInv", query = "SELECT s FROM Sistema s WHERE s.sisalmtrInv = :sisalmtrInv"),
    @NamedQuery(name = "Sistema.findBySisprtdfVen", query = "SELECT s FROM Sistema s WHERE s.sisprtdfVen = :sisprtdfVen"),
    @NamedQuery(name = "Sistema.findBySisprtdcVen", query = "SELECT s FROM Sistema s WHERE s.sisprtdcVen = :sisprtdcVen"),
    @NamedQuery(name = "Sistema.findBySislongrucCaj", query = "SELECT s FROM Sistema s WHERE s.sislongrucCaj = :sislongrucCaj"),
    @NamedQuery(name = "Sistema.findBySisredCaj", query = "SELECT s FROM Sistema s WHERE s.sisredCaj = :sisredCaj"),
    @NamedQuery(name = "Sistema.findBySisincenColor", query = "SELECT s FROM Sistema s WHERE s.sisincenColor = :sisincenColor"),
    @NamedQuery(name = "Sistema.findBySisincen", query = "SELECT s FROM Sistema s WHERE s.sisincen = :sisincen"),
    @NamedQuery(name = "Sistema.findByGruncod", query = "SELECT s FROM Sistema s WHERE s.gruncod = :gruncod"),
    @NamedQuery(name = "Sistema.findByNdiasanuCaj", query = "SELECT s FROM Sistema s WHERE s.ndiasanuCaj = :ndiasanuCaj"),
    @NamedQuery(name = "Sistema.findByAsocdocVen", query = "SELECT s FROM Sistema s WHERE s.asocdocVen = :asocdocVen"),
    @NamedQuery(name = "Sistema.findByTdoserVen", query = "SELECT s FROM Sistema s WHERE s.tdoserVen = :tdoserVen"),
    @NamedQuery(name = "Sistema.findBySisindstkpedCom", query = "SELECT s FROM Sistema s WHERE s.sisindstkpedCom = :sisindstkpedCom"),
    @NamedQuery(name = "Sistema.findBySiscolstkpedCom", query = "SELECT s FROM Sistema s WHERE s.siscolstkpedCom = :siscolstkpedCom"),
    @NamedQuery(name = "Sistema.findBySisalcfacCli", query = "SELECT s FROM Sistema s WHERE s.sisalcfacCli = :sisalcfacCli"),
    @NamedQuery(name = "Sistema.findBySisverimptoVen", query = "SELECT s FROM Sistema s WHERE s.sisverimptoVen = :sisverimptoVen"),
    @NamedQuery(name = "Sistema.findBySisprodalmInv", query = "SELECT s FROM Sistema s WHERE s.sisprodalmInv = :sisprodalmInv"),
    @NamedQuery(name = "Sistema.findBySiscagInv", query = "SELECT s FROM Sistema s WHERE s.siscagInv = :siscagInv"),
    @NamedQuery(name = "Sistema.findBySiscafInv", query = "SELECT s FROM Sistema s WHERE s.siscafInv = :siscafInv"),
    @NamedQuery(name = "Sistema.findBySiscalInv", query = "SELECT s FROM Sistema s WHERE s.siscalInv = :siscalInv"),
    @NamedQuery(name = "Sistema.findBySisdisocCom", query = "SELECT s FROM Sistema s WHERE s.sisdisocCom = :sisdisocCom"),
    @NamedQuery(name = "Sistema.findBySischkstaVen", query = "SELECT s FROM Sistema s WHERE s.sischkstaVen = :sischkstaVen"),
    @NamedQuery(name = "Sistema.findBySischkcodVen", query = "SELECT s FROM Sistema s WHERE s.sischkcodVen = :sischkcodVen"),
    @NamedQuery(name = "Sistema.findBySischkstaCom", query = "SELECT s FROM Sistema s WHERE s.sischkstaCom = :sischkstaCom"),
    @NamedQuery(name = "Sistema.findBySischkcodCom", query = "SELECT s FROM Sistema s WHERE s.sischkcodCom = :sischkcodCom"),
    @NamedQuery(name = "Sistema.findBySischkstaInv", query = "SELECT s FROM Sistema s WHERE s.sischkstaInv = :sischkstaInv"),
    @NamedQuery(name = "Sistema.findBySischkcodInv", query = "SELECT s FROM Sistema s WHERE s.sischkcodInv = :sischkcodInv"),
    @NamedQuery(name = "Sistema.findBySispasscdInv", query = "SELECT s FROM Sistema s WHERE s.sispasscdInv = :sispasscdInv"),
    @NamedQuery(name = "Sistema.findBySistactateVen", query = "SELECT s FROM Sistema s WHERE s.sistactateVen = :sistactateVen"),
    @NamedQuery(name = "Sistema.findBySispubfacVen", query = "SELECT s FROM Sistema s WHERE s.sispubfacVen = :sispubfacVen"),
    @NamedQuery(name = "Sistema.findBySissecCommit", query = "SELECT s FROM Sistema s WHERE s.sissecCommit = :sissecCommit"),
    @NamedQuery(name = "Sistema.findBySiscccom", query = "SELECT s FROM Sistema s WHERE s.siscccom = :siscccom"),
    @NamedQuery(name = "Sistema.findBySiscomordcomCom", query = "SELECT s FROM Sistema s WHERE s.siscomordcomCom = :siscomordcomCom"),
    @NamedQuery(name = "Sistema.findBySisPreimplot", query = "SELECT s FROM Sistema s WHERE s.sisPreimplot = :sisPreimplot"),
    @NamedQuery(name = "Sistema.findBySisPreimplotCaj", query = "SELECT s FROM Sistema s WHERE s.sisPreimplotCaj = :sisPreimplotCaj"),
    @NamedQuery(name = "Sistema.findBySisdoccliCaj", query = "SELECT s FROM Sistema s WHERE s.sisdoccliCaj = :sisdoccliCaj"),
    @NamedQuery(name = "Sistema.findBySismontminCaj", query = "SELECT s FROM Sistema s WHERE s.sismontminCaj = :sismontminCaj"),
    @NamedQuery(name = "Sistema.findBySisreglotfvenInv", query = "SELECT s FROM Sistema s WHERE s.sisreglotfvenInv = :sisreglotfvenInv"),
    @NamedQuery(name = "Sistema.findBySisactprecosInv", query = "SELECT s FROM Sistema s WHERE s.sisactprecosInv = :sisactprecosInv"),
    @NamedQuery(name = "Sistema.findBySistdoccliCaj", query = "SELECT s FROM Sistema s WHERE s.sistdoccliCaj = :sistdoccliCaj"),
    @NamedQuery(name = "Sistema.findBySisviscomfcVen", query = "SELECT s FROM Sistema s WHERE s.sisviscomfcVen = :sisviscomfcVen"),
    @NamedQuery(name = "Sistema.findBySisfileprescPre", query = "SELECT s FROM Sistema s WHERE s.sisfileprescPre = :sisfileprescPre"),
    @NamedQuery(name = "Sistema.findBySistipredCaj", query = "SELECT s FROM Sistema s WHERE s.sistipredCaj = :sistipredCaj"),
    @NamedQuery(name = "Sistema.findBySismodvvfocCom", query = "SELECT s FROM Sistema s WHERE s.sismodvvfocCom = :sismodvvfocCom"),
    @NamedQuery(name = "Sistema.findBySisverruc", query = "SELECT s FROM Sistema s WHERE s.sisverruc = :sisverruc"),
    @NamedQuery(name = "Sistema.findBySisprdctrctVen", query = "SELECT s FROM Sistema s WHERE s.sisprdctrctVen = :sisprdctrctVen"),
    @NamedQuery(name = "Sistema.findBySisprdctrfcVen", query = "SELECT s FROM Sistema s WHERE s.sisprdctrfcVen = :sisprdctrfcVen"),
    @NamedQuery(name = "Sistema.findBySisresprdctrdlvVen", query = "SELECT s FROM Sistema s WHERE s.sisresprdctrdlvVen = :sisresprdctrdlvVen"),
    @NamedQuery(name = "Sistema.findBySisporcrepentCom", query = "SELECT s FROM Sistema s WHERE s.sisporcrepentCom = :sisporcrepentCom"),
    @NamedQuery(name = "Sistema.findBySisprdnarcfcVen", query = "SELECT s FROM Sistema s WHERE s.sisprdnarcfcVen = :sisprdnarcfcVen"),
    @NamedQuery(name = "Sistema.findBySisprdnarcctVen", query = "SELECT s FROM Sistema s WHERE s.sisprdnarcctVen = :sisprdnarcctVen"),
    @NamedQuery(name = "Sistema.findBySismoncredlvCli", query = "SELECT s FROM Sistema s WHERE s.sismoncredlvCli = :sismoncredlvCli"),
    @NamedQuery(name = "Sistema.findBySisfficredlvCli", query = "SELECT s FROM Sistema s WHERE s.sisfficredlvCli = :sisfficredlvCli"),
    @NamedQuery(name = "Sistema.findBySiscodpais", query = "SELECT s FROM Sistema s WHERE s.siscodpais = :siscodpais"),
    @NamedQuery(name = "Sistema.findBySislocalidad", query = "SELECT s FROM Sistema s WHERE s.sislocalidad = :sislocalidad"),
    @NamedQuery(name = "Sistema.findBySisanudocuseCaj", query = "SELECT s FROM Sistema s WHERE s.sisanudocuseCaj = :sisanudocuseCaj"),
    @NamedQuery(name = "Sistema.findBySiseditdofacCaj", query = "SELECT s FROM Sistema s WHERE s.siseditdofacCaj = :siseditdofacCaj"),
    @NamedQuery(name = "Sistema.findBySisactconantcateVen", query = "SELECT s FROM Sistema s WHERE s.sisactconantcateVen = :sisactconantcateVen"),
    @NamedQuery(name = "Sistema.findBySismoddatcliVen", query = "SELECT s FROM Sistema s WHERE s.sismoddatcliVen = :sismoddatcliVen"),
    @NamedQuery(name = "Sistema.findBySistipgenface", query = "SELECT s FROM Sistema s WHERE s.sistipgenface = :sistipgenface"),
    @NamedQuery(name = "Sistema.findBySisrsumporcVen", query = "SELECT s FROM Sistema s WHERE s.sisrsumporcVen = :sisrsumporcVen"),
    @NamedQuery(name = "Sistema.findBySisverfvenstaInv", query = "SELECT s FROM Sistema s WHERE s.sisverfvenstaInv = :sisverfvenstaInv"),
    @NamedQuery(name = "Sistema.findBySisverfvendiasInv", query = "SELECT s FROM Sistema s WHERE s.sisverfvendiasInv = :sisverfvendiasInv"),
    @NamedQuery(name = "Sistema.findBySisvalperstaInv", query = "SELECT s FROM Sistema s WHERE s.sisvalperstaInv = :sisvalperstaInv"),
    @NamedQuery(name = "Sistema.findBySismoddetcomInv", query = "SELECT s FROM Sistema s WHERE s.sismoddetcomInv = :sismoddetcomInv"),
    @NamedQuery(name = "Sistema.findBySisresface", query = "SELECT s FROM Sistema s WHERE s.sisresface = :sisresface"),
    @NamedQuery(name = "Sistema.findBySisurlface", query = "SELECT s FROM Sistema s WHERE s.sisurlface = :sisurlface"),
    @NamedQuery(name = "Sistema.findBySislogo", query = "SELECT s FROM Sistema s WHERE s.sislogo = :sislogo"),
    @NamedQuery(name = "Sistema.findBySismsg3", query = "SELECT s FROM Sistema s WHERE s.sismsg3 = :sismsg3"),
    @NamedQuery(name = "Sistema.findBySisdlvsta", query = "SELECT s FROM Sistema s WHERE s.sisdlvsta = :sisdlvsta"),
    @NamedQuery(name = "Sistema.findBySisnocaldtoccnoprgInv", query = "SELECT s FROM Sistema s WHERE s.sisnocaldtoccnoprgInv = :sisnocaldtoccnoprgInv"),
    @NamedQuery(name = "Sistema.findBySisvtatabletVen", query = "SELECT s FROM Sistema s WHERE s.sisvtatabletVen = :sisvtatabletVen"),
    @NamedQuery(name = "Sistema.findBySismsgvalstksecVen", query = "SELECT s FROM Sistema s WHERE s.sismsgvalstksecVen = :sismsgvalstksecVen"),
    @NamedQuery(name = "Sistema.findBySistedefIdcalt", query = "SELECT s FROM Sistema s WHERE s.sistedefIdcalt = :sistedefIdcalt"),
    @NamedQuery(name = "Sistema.findBySisfiletedefLeg", query = "SELECT s FROM Sistema s WHERE s.sisfiletedefLeg = :sisfiletedefLeg"),
    @NamedQuery(name = "Sistema.findBySiscodugipres", query = "SELECT s FROM Sistema s WHERE s.siscodugipres = :siscodugipres"),
    @NamedQuery(name = "Sistema.findBySisnamface", query = "SELECT s FROM Sistema s WHERE s.sisnamface = :sisnamface"),
    @NamedQuery(name = "Sistema.findBySispasface", query = "SELECT s FROM Sistema s WHERE s.sispasface = :sispasface"),
    @NamedQuery(name = "Sistema.findBySisvalestfaceVen", query = "SELECT s FROM Sistema s WHERE s.sisvalestfaceVen = :sisvalestfaceVen"),
    @NamedQuery(name = "Sistema.findBySissoldirCaj", query = "SELECT s FROM Sistema s WHERE s.sissoldirCaj = :sissoldirCaj"),
    @NamedQuery(name = "Sistema.findBySistdofacCaj", query = "SELECT s FROM Sistema s WHERE s.sistdofacCaj = :sistdofacCaj"),
    @NamedQuery(name = "Sistema.findBySisstacolestraVen", query = "SELECT s FROM Sistema s WHERE s.sisstacolestraVen = :sisstacolestraVen"),
    @NamedQuery(name = "Sistema.findBySiscolorestraVen", query = "SELECT s FROM Sistema s WHERE s.siscolorestraVen = :siscolorestraVen"),
    @NamedQuery(name = "Sistema.findBySisstacolprefeVen", query = "SELECT s FROM Sistema s WHERE s.sisstacolprefeVen = :sisstacolprefeVen"),
    @NamedQuery(name = "Sistema.findBySiscolorprefeVen", query = "SELECT s FROM Sistema s WHERE s.siscolorprefeVen = :siscolorprefeVen"),
    @NamedQuery(name = "Sistema.findBySisimpmsjptosCaj", query = "SELECT s FROM Sistema s WHERE s.sisimpmsjptosCaj = :sisimpmsjptosCaj"),
    @NamedQuery(name = "Sistema.findBySismaxptosalertCli", query = "SELECT s FROM Sistema s WHERE s.sismaxptosalertCli = :sismaxptosalertCli"),
    @NamedQuery(name = "Sistema.findBySisidcaltbar", query = "SELECT s FROM Sistema s WHERE s.sisidcaltbar = :sisidcaltbar"),
    @NamedQuery(name = "Sistema.findBySisactstaccnoprgInv", query = "SELECT s FROM Sistema s WHERE s.sisactstaccnoprgInv = :sisactstaccnoprgInv"),
    @NamedQuery(name = "Sistema.findBySispathSited", query = "SELECT s FROM Sistema s WHERE s.sispathSited = :sispathSited"),
    @NamedQuery(name = "Sistema.findBySiscodipress", query = "SELECT s FROM Sistema s WHERE s.siscodipress = :siscodipress"),
    @NamedQuery(name = "Sistema.findBySisuseripress", query = "SELECT s FROM Sistema s WHERE s.sisuseripress = :sisuseripress"),
    @NamedQuery(name = "Sistema.findBySispasipress", query = "SELECT s FROM Sistema s WHERE s.sispasipress = :sispasipress"),
    @NamedQuery(name = "Sistema.findBySistdofacVen", query = "SELECT s FROM Sistema s WHERE s.sistdofacVen = :sistdofacVen"),
    @NamedQuery(name = "Sistema.findBySisreqregmedfcVen", query = "SELECT s FROM Sistema s WHERE s.sisreqregmedfcVen = :sisreqregmedfcVen"),
    @NamedQuery(name = "Sistema.findBySisreqregmedctVen", query = "SELECT s FROM Sistema s WHERE s.sisreqregmedctVen = :sisreqregmedctVen"),
    @NamedQuery(name = "Sistema.findBySisactconantvtasVen", query = "SELECT s FROM Sistema s WHERE s.sisactconantvtasVen = :sisactconantvtasVen"),
    @NamedQuery(name = "Sistema.findBySisactconantcotiVen", query = "SELECT s FROM Sistema s WHERE s.sisactconantcotiVen = :sisactconantcotiVen"),
    @NamedQuery(name = "Sistema.findBySissesionsta", query = "SELECT s FROM Sistema s WHERE s.sissesionsta = :sissesionsta"),
    @NamedQuery(name = "Sistema.findBySisdtoadicautVen", query = "SELECT s FROM Sistema s WHERE s.sisdtoadicautVen = :sisdtoadicautVen"),
    @NamedQuery(name = "Sistema.findBySistipacumptosCli", query = "SELECT s FROM Sistema s WHERE s.sistipacumptosCli = :sistipacumptosCli"),
    @NamedQuery(name = "Sistema.findBySismontobasptosCli", query = "SELECT s FROM Sistema s WHERE s.sismontobasptosCli = :sismontobasptosCli"),
    @NamedQuery(name = "Sistema.findBySiseqvptosCli", query = "SELECT s FROM Sistema s WHERE s.siseqvptosCli = :siseqvptosCli"),
    @NamedQuery(name = "Sistema.findByCodalmqullfarVen", query = "SELECT s FROM Sistema s WHERE s.codalmqullfarVen = :codalmqullfarVen"),
    @NamedQuery(name = "Sistema.findByUsecodqullfarVen", query = "SELECT s FROM Sistema s WHERE s.usecodqullfarVen = :usecodqullfarVen"),
    @NamedQuery(name = "Sistema.findByUrlimgrecqullfarVen", query = "SELECT s FROM Sistema s WHERE s.urlimgrecqullfarVen = :urlimgrecqullfarVen"),
    @NamedQuery(name = "Sistema.findBySisappmpCaj", query = "SELECT s FROM Sistema s WHERE s.sisappmpCaj = :sisappmpCaj"),
    @NamedQuery(name = "Sistema.findBySissidmpCaj", query = "SELECT s FROM Sistema s WHERE s.sissidmpCaj = :sissidmpCaj"),
    @NamedQuery(name = "Sistema.findBySisrestcantpedCom", query = "SELECT s FROM Sistema s WHERE s.sisrestcantpedCom = :sisrestcantpedCom"),
    @NamedQuery(name = "Sistema.findBySissmtpserver", query = "SELECT s FROM Sistema s WHERE s.sissmtpserver = :sissmtpserver"),
    @NamedQuery(name = "Sistema.findBySissmtpport", query = "SELECT s FROM Sistema s WHERE s.sissmtpport = :sissmtpport"),
    @NamedQuery(name = "Sistema.findBySissmtpuserid", query = "SELECT s FROM Sistema s WHERE s.sissmtpuserid = :sissmtpuserid"),
    @NamedQuery(name = "Sistema.findBySissmtppass", query = "SELECT s FROM Sistema s WHERE s.sissmtppass = :sissmtppass"),
    @NamedQuery(name = "Sistema.findBySisstasmtpusrpass", query = "SELECT s FROM Sistema s WHERE s.sisstasmtpusrpass = :sisstasmtpusrpass"),
    @NamedQuery(name = "Sistema.findBySissmtptencrypt", query = "SELECT s FROM Sistema s WHERE s.sissmtptencrypt = :sissmtptencrypt"),
    @NamedQuery(name = "Sistema.findBySisasiglomayVen", query = "SELECT s FROM Sistema s WHERE s.sisasiglomayVen = :sisasiglomayVen"),
    @NamedQuery(name = "Sistema.findBySismantprecdtocotVen", query = "SELECT s FROM Sistema s WHERE s.sismantprecdtocotVen = :sismantprecdtocotVen"),
    @NamedQuery(name = "Sistema.findBySismsgcotVen", query = "SELECT s FROM Sistema s WHERE s.sismsgcotVen = :sismsgcotVen"),
    @NamedQuery(name = "Sistema.findBySismsgadicCaj", query = "SELECT s FROM Sistema s WHERE s.sismsgadicCaj = :sismsgadicCaj"),
    @NamedQuery(name = "Sistema.findBySisctaenvocCom", query = "SELECT s FROM Sistema s WHERE s.sisctaenvocCom = :sisctaenvocCom"),
    @NamedQuery(name = "Sistema.findBySispathpdflocCom", query = "SELECT s FROM Sistema s WHERE s.sispathpdflocCom = :sispathpdflocCom"),
    @NamedQuery(name = "Sistema.findBySiscodaltbarrInv", query = "SELECT s FROM Sistema s WHERE s.siscodaltbarrInv = :siscodaltbarrInv"),
    @NamedQuery(name = "Sistema.findBySisprecbasVen", query = "SELECT s FROM Sistema s WHERE s.sisprecbasVen = :sisprecbasVen"),
    @NamedQuery(name = "Sistema.findBySiscodalminsFmg", query = "SELECT s FROM Sistema s WHERE s.siscodalminsFmg = :siscodalminsFmg"),
    @NamedQuery(name = "Sistema.findBySiscalcosordprodInv", query = "SELECT s FROM Sistema s WHERE s.siscalcosordprodInv = :siscalcosordprodInv"),
    @NamedQuery(name = "Sistema.findBySismondifcompInv", query = "SELECT s FROM Sistema s WHERE s.sismondifcompInv = :sismondifcompInv"),
    @NamedQuery(name = "Sistema.findBySistimedflentFmg", query = "SELECT s FROM Sistema s WHERE s.sistimedflentFmg = :sistimedflentFmg"),
    @NamedQuery(name = "Sistema.findBySiscodundodsFmg", query = "SELECT s FROM Sistema s WHERE s.siscodundodsFmg = :siscodundodsFmg"),
    @NamedQuery(name = "Sistema.findBySiscodtprepodsFmg", query = "SELECT s FROM Sistema s WHERE s.siscodtprepodsFmg = :siscodtprepodsFmg"),
    @NamedQuery(name = "Sistema.findBySisselautlotInv", query = "SELECT s FROM Sistema s WHERE s.sisselautlotInv = :sisselautlotInv"),
    @NamedQuery(name = "Sistema.findBySistimemaxajusfpCaj", query = "SELECT s FROM Sistema s WHERE s.sistimemaxajusfpCaj = :sistimemaxajusfpCaj"),
    @NamedQuery(name = "Sistema.findBySissmtpmethodaut", query = "SELECT s FROM Sistema s WHERE s.sissmtpmethodaut = :sissmtpmethodaut"),
    @NamedQuery(name = "Sistema.findBySissmtpcharset", query = "SELECT s FROM Sistema s WHERE s.sissmtpcharset = :sissmtpcharset"),
    @NamedQuery(name = "Sistema.findBySissmtpconntype", query = "SELECT s FROM Sistema s WHERE s.sissmtpconntype = :sissmtpconntype"),
    @NamedQuery(name = "Sistema.findBySisfilelibeLeg", query = "SELECT s FROM Sistema s WHERE s.sisfilelibeLeg = :sisfilelibeLeg"),
    @NamedQuery(name = "Sistema.findBySiscodtri", query = "SELECT s FROM Sistema s WHERE s.siscodtri = :siscodtri"),
    @NamedQuery(name = "Sistema.findBySisanunccajcerrVen", query = "SELECT s FROM Sistema s WHERE s.sisanunccajcerrVen = :sisanunccajcerrVen"),
    @NamedQuery(name = "Sistema.findBySisanudoccajcerrCaj", query = "SELECT s FROM Sistema s WHERE s.sisanudoccajcerrCaj = :sisanudoccajcerrCaj"),
    @NamedQuery(name = "Sistema.findBySisminentrDlv", query = "SELECT s FROM Sistema s WHERE s.sisminentrDlv = :sisminentrDlv"),
    @NamedQuery(name = "Sistema.findBySishinientrDlv", query = "SELECT s FROM Sistema s WHERE s.sishinientrDlv = :sishinientrDlv"),
    @NamedQuery(name = "Sistema.findBySishfinentrDlv", query = "SELECT s FROM Sistema s WHERE s.sishfinentrDlv = :sishfinentrDlv"),
    @NamedQuery(name = "Sistema.findBySiscarDlv", query = "SELECT s FROM Sistema s WHERE s.siscarDlv = :siscarDlv"),
    @NamedQuery(name = "Sistema.findBySisstacardCli", query = "SELECT s FROM Sistema s WHERE s.sisstacardCli = :sisstacardCli"),
    @NamedQuery(name = "Sistema.findBySisptosdtoCli", query = "SELECT s FROM Sistema s WHERE s.sisptosdtoCli = :sisptosdtoCli"),
    @NamedQuery(name = "Sistema.findBySismoneqvdtoptosCli", query = "SELECT s FROM Sistema s WHERE s.sismoneqvdtoptosCli = :sismoneqvdtoptosCli"),
    @NamedQuery(name = "Sistema.findBySisdtomaxptosCli", query = "SELECT s FROM Sistema s WHERE s.sisdtomaxptosCli = :sisdtomaxptosCli")})
public class Sistema implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "siscod")
    private Integer siscod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 70)
    @Column(name = "sistit")
    private String sistit;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 70)
    @Column(name = "sisent")
    private String sisent;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 70)
    @Column(name = "sisdir")
    private String sisdir;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "sisruc")
    private String sisruc;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "sisigv")
    private BigDecimal sisigv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "sisenc")
    private String sisenc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sisusr")
    private int sisusr;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "sistel")
    private String sistel;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "sisver")
    private String sisver;
    @Basic(optional = false)
    @NotNull
    @Column(name = "siscolor")
    private int siscolor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado")
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "feccre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecumv")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecumv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "usecod")
    private int usecod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "usenam")
    private String usenam;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "hostname")
    private String hostname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "siscap_pac")
    private String siscapPac;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sisalf_pac")
    private int sisalfPac;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sisacf_pac")
    private int sisacfPac;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sistdf_pac")
    private String sistdfPac;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "sisdif_pac")
    private String sisdifPac;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "siscam_int")
    private String siscamInt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sisnlf_ven")
    private int sisnlfVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "sistar_ven")
    private String sistarVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisert_ven")
    private String sisertVen;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sisprt_ven")
    private BigDecimal sisprtVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sislrp_ven")
    private String sislrpVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisdvm_ven")
    private String sisdvmVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "siserm_ven")
    private String sisermVen;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sisprm_ven")
    private BigDecimal sisprmVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "siserd_ven")
    private String siserdVen;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sisprd_ven")
    private BigDecimal sisprdVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "siserh_ven")
    private String siserhVen;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sisprh_ven")
    private BigDecimal sisprhVen;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sisirh_ven")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sisirhVen;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sisfrh_ven")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sisfrhVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisecu_inv")
    private String sisecuInv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sistcu_inv")
    private String sistcuInv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "siscac_cli")
    private String siscacCli;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sismrm_ven")
    private String sismrmVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisfic_ven")
    private String sisficVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "sismon_s")
    private String sismonS;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "sismon_d")
    private String sismonD;
    @Size(max = 80)
    @Column(name = "sispath_update")
    private String sispathUpdate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisrem_com")
    private String sisremCom;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sismdoc_caj")
    private String sismdocCaj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sismodpr_inv")
    private String sismodprInv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sisvarpr_inv")
    private BigDecimal sisvarprInv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisdocop_caj")
    private String sisdocopCaj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "siscotstk_ven")
    private String siscotstkVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sispassfc_ven")
    private String sispassfcVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sispassct_ven")
    private String sispassctVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisreginf_caj")
    private String sisreginfCaj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisprescfc_ven")
    private String sisprescfcVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisprescct_ven")
    private String sisprescctVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "siscarcli_ven")
    private String siscarcliVen;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sismrd_ven")
    private BigDecimal sismrdVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sismsm_ven")
    private String sismsmVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisvlo")
    private String sisvlo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisstkneg_ven")
    private String sisstknegVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sistrdto_inv")
    private String sistrdtoInv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisent_tip")
    private String sisentTip;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisdisco_inv")
    private String sisdiscoInv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisregdto_inv")
    private String sisregdtoInv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisticom")
    private String sisticom;
    @Size(max = 50)
    @Column(name = "sismail")
    private String sismail;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisnewct_ven")
    private String sisnewctVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisnewfc_ven")
    private String sisnewfcVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "siswsec_ven")
    private String siswsecVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisanuvta_caj")
    private String sisanuvtaCaj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisconimp_caj")
    private String sisconimpCaj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisvalruc_caj")
    private String sisvalrucCaj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "sistidval_caj")
    private String sistidvalCaj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 90)
    @Column(name = "sisdirf")
    private String sisdirf;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "sismsg1")
    private String sismsg1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "sismsg2")
    private String sismsg2;
    @Size(max = 2)
    @Column(name = "sisalmtr_inv")
    private String sisalmtrInv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisprtdf_ven")
    private String sisprtdfVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisprtdc_ven")
    private String sisprtdcVen;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sislongruc_caj")
    private int sislongrucCaj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisred_caj")
    private String sisredCaj;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sisincen_color")
    private int sisincenColor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisincen")
    private String sisincen;
    @Size(max = 2)
    @Column(name = "gruncod")
    private String gruncod;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ndiasanu_caj")
    private int ndiasanuCaj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "asocdoc_ven")
    private String asocdocVen;
    @Size(max = 2)
    @Column(name = "tdoser_ven")
    private String tdoserVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisindstkped_com")
    private String sisindstkpedCom;
    @Basic(optional = false)
    @NotNull
    @Column(name = "siscolstkped_com")
    private int siscolstkpedCom;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisalcfac_cli")
    private String sisalcfacCli;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisverimpto_ven")
    private String sisverimptoVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisprodalm_inv")
    private String sisprodalmInv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "siscag_inv")
    private String siscagInv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "siscaf_inv")
    private String siscafInv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "siscal_inv")
    private String siscalInv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisdisoc_com")
    private String sisdisocCom;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sischksta_ven")
    private String sischkstaVen;
    @Column(name = "sischkcod_ven")
    private Integer sischkcodVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sischksta_com")
    private String sischkstaCom;
    @Column(name = "sischkcod_com")
    private Integer sischkcodCom;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sischksta_inv")
    private String sischkstaInv;
    @Column(name = "sischkcod_inv")
    private Integer sischkcodInv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sispasscd_inv")
    private String sispasscdInv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sistactate_ven")
    private int sistactateVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sispubfac_ven")
    private String sispubfacVen;
    @Size(max = 60)
    @Column(name = "sissec_commit")
    private String sissecCommit;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "siscccom")
    private String siscccom;
    @Size(max = 1000)
    @Column(name = "siscomordcom_com")
    private String siscomordcomCom;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sis_preimplot")
    private String sisPreimplot;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sis_preimplot_caj")
    private String sisPreimplotCaj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisdoccli_caj")
    private String sisdoccliCaj;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sismontmin_caj")
    private BigDecimal sismontminCaj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisreglotfven_inv")
    private String sisreglotfvenInv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisactprecos_inv")
    private String sisactprecosInv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "sistdoccli_caj")
    private String sistdoccliCaj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisviscomfc_ven")
    private String sisviscomfcVen;
    @Size(max = 100)
    @Column(name = "sisfilepresc_pre")
    private String sisfileprescPre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sistipred_caj")
    private String sistipredCaj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sismodvvfoc_com")
    private String sismodvvfocCom;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisverruc")
    private String sisverruc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisprdctrct_ven")
    private String sisprdctrctVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisprdctrfc_ven")
    private String sisprdctrfcVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisresprdctrdlv_ven")
    private String sisresprdctrdlvVen;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sisporcrepent_com")
    private BigDecimal sisporcrepentCom;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisprdnarcfc_ven")
    private String sisprdnarcfcVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisprdnarcct_ven")
    private String sisprdnarcctVen;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sismoncredlv_cli")
    private BigDecimal sismoncredlvCli;
    @Column(name = "sisfficredlv_cli")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sisfficredlvCli;
    @Size(max = 2)
    @Column(name = "siscodpais")
    private String siscodpais;
    @Size(max = 40)
    @Column(name = "sislocalidad")
    private String sislocalidad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisanudocuse_caj")
    private String sisanudocuseCaj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "siseditdofac_caj")
    private String siseditdofacCaj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisactconantcate_ven")
    private String sisactconantcateVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sismoddatcli_ven")
    private String sismoddatcliVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sistipgenface")
    private String sistipgenface;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisrsumporc_ven")
    private String sisrsumporcVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisverfvensta_inv")
    private String sisverfvenstaInv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sisverfvendias_inv")
    private int sisverfvendiasInv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisvalpersta_inv")
    private String sisvalperstaInv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sismoddetcom_inv")
    private String sismoddetcomInv;
    @Size(max = 30)
    @Column(name = "sisresface")
    private String sisresface;
    @Size(max = 400)
    @Column(name = "sisurlface")
    private String sisurlface;
    @Size(max = 256)
    @Column(name = "sislogo")
    private String sislogo;
    @Size(max = 1000)
    @Column(name = "sismsg3")
    private String sismsg3;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisdlvsta")
    private String sisdlvsta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisnocaldtoccnoprg_inv")
    private String sisnocaldtoccnoprgInv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisvtatablet_ven")
    private String sisvtatabletVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sismsgvalstksec_ven")
    private String sismsgvalstksecVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sistedef_idcalt")
    private String sistedefIdcalt;
    @Size(max = 150)
    @Column(name = "sisfiletedef_leg")
    private String sisfiletedefLeg;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "siscodugipres")
    private String siscodugipres;
    @Size(max = 30)
    @Column(name = "sisnamface")
    private String sisnamface;
    @Size(max = 100)
    @Column(name = "sispasface")
    private String sispasface;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisvalestface_ven")
    private String sisvalestfaceVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sissoldir_caj")
    private String sissoldirCaj;
    @Size(max = 100)
    @Column(name = "sistdofac_caj")
    private String sistdofacCaj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisstacolestra_ven")
    private String sisstacolestraVen;
    @Basic(optional = false)
    @NotNull
    @Column(name = "siscolorestra_ven")
    private int siscolorestraVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisstacolprefe_ven")
    private String sisstacolprefeVen;
    @Basic(optional = false)
    @NotNull
    @Column(name = "siscolorprefe_ven")
    private int siscolorprefeVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisimpmsjptos_caj")
    private String sisimpmsjptosCaj;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sismaxptosalert_cli")
    private BigDecimal sismaxptosalertCli;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisidcaltbar")
    private String sisidcaltbar;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisactstaccnoprg_inv")
    private String sisactstaccnoprgInv;
    @Size(max = 200)
    @Column(name = "sispath_sited")
    private String sispathSited;
    @Size(max = 8)
    @Column(name = "siscodipress")
    private String siscodipress;
    @Size(max = 20)
    @Column(name = "sisuseripress")
    private String sisuseripress;
    @Size(max = 30)
    @Column(name = "sispasipress")
    private String sispasipress;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "sistdofac_ven")
    private String sistdofacVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisreqregmedfc_ven")
    private String sisreqregmedfcVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisreqregmedct_ven")
    private String sisreqregmedctVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisactconantvtas_ven")
    private String sisactconantvtasVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisactconantcoti_ven")
    private String sisactconantcotiVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sissesionsta")
    private String sissesionsta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisdtoadicaut_ven")
    private String sisdtoadicautVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sistipacumptos_cli")
    private String sistipacumptosCli;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sismontobasptos_cli")
    private BigDecimal sismontobasptosCli;
    @Basic(optional = false)
    @NotNull
    @Column(name = "siseqvptos_cli")
    private BigDecimal siseqvptosCli;
    @Size(max = 2)
    @Column(name = "codalmqullfar_ven")
    private String codalmqullfarVen;
    @Column(name = "usecodqullfar_ven")
    private Integer usecodqullfarVen;
    @Size(max = 120)
    @Column(name = "urlimgrecqullfar_ven")
    private String urlimgrecqullfarVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "sisappmp_caj")
    private String sisappmpCaj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "sissidmp_caj")
    private String sissidmpCaj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisrestcantped_com")
    private String sisrestcantpedCom;
    @Size(max = 60)
    @Column(name = "sissmtpserver")
    private String sissmtpserver;
    @Size(max = 10)
    @Column(name = "sissmtpport")
    private String sissmtpport;
    @Size(max = 60)
    @Column(name = "sissmtpuserid")
    private String sissmtpuserid;
    @Size(max = 50)
    @Column(name = "sissmtppass")
    private String sissmtppass;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisstasmtpusrpass")
    private String sisstasmtpusrpass;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "sissmtptencrypt")
    private String sissmtptencrypt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisasiglomay_ven")
    private String sisasiglomayVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sismantprecdtocot_ven")
    private String sismantprecdtocotVen;
    @Size(max = 1000)
    @Column(name = "sismsgcot_ven")
    private String sismsgcotVen;
    @Size(max = 1000)
    @Column(name = "sismsgadic_caj")
    private String sismsgadicCaj;
    @Size(max = 120)
    @Column(name = "sisctaenvoc_com")
    private String sisctaenvocCom;
    @Size(max = 256)
    @Column(name = "sispathpdfloc_com")
    private String sispathpdflocCom;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "sistextenvoc_com")
    private String sistextenvocCom;
    @Size(max = 21)
    @Column(name = "siscodaltbarr_inv")
    private String siscodaltbarrInv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "sisprecbas_ven")
    private String sisprecbasVen;
    @Size(max = 2)
    @Column(name = "siscodalmins_fmg")
    private String siscodalminsFmg;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "siscalcosordprod_inv")
    private String siscalcosordprodInv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sismondifcomp_inv")
    private BigDecimal sismondifcompInv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sistimedflent_fmg")
    private int sistimedflentFmg;
    @Size(max = 3)
    @Column(name = "siscodundods_fmg")
    private String siscodundodsFmg;
    @Size(max = 4)
    @Column(name = "siscodtprepods_fmg")
    private String siscodtprepodsFmg;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisselautlot_inv")
    private String sisselautlotInv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sistimemaxajusfp_caj")
    private int sistimemaxajusfpCaj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sissmtpmethodaut")
    private String sissmtpmethodaut;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "sissmtpcharset")
    private String sissmtpcharset;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sissmtpconntype")
    private String sissmtpconntype;
    @Size(max = 150)
    @Column(name = "sisfilelibe_leg")
    private String sisfilelibeLeg;
    @Size(max = 4)
    @Column(name = "siscodtri")
    private String siscodtri;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisanunccajcerr_ven")
    private String sisanunccajcerrVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisanudoccajcerr_caj")
    private String sisanudoccajcerrCaj;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sisminentr_dlv")
    private long sisminentrDlv;
    @Column(name = "sishinientr_dlv")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sishinientrDlv;
    @Column(name = "sishfinentr_dlv")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sishfinentrDlv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "siscar_dlv")
    private String siscarDlv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisstacard_cli")
    private String sisstacardCli;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sisptosdto_cli")
    private BigDecimal sisptosdtoCli;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sismoneqvdtoptos_cli")
    private BigDecimal sismoneqvdtoptosCli;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sisdtomaxptos_cli")
    private BigDecimal sisdtomaxptosCli;
    @JoinColumn(name = "sisforpag_ven", referencedColumnName = "docpag")
    @ManyToOne(optional = false)
    private DocumentosPago sisforpagVen;
    @JoinColumn(name = "sisforpagcrecli_ven", referencedColumnName = "docpag")
    @ManyToOne(optional = false)
    private DocumentosPago sisforpagcrecliVen;
    @JoinColumn(name = "sistidcod", referencedColumnName = "tidcod")
    @ManyToOne(optional = false)
    private TipoDocumentoIdentidad sistidcod;
    @JoinColumn(name = "sisubicod", referencedColumnName = "ubicod")
    @ManyToOne
    private Ubigeo sisubicod;

    public Sistema() {
    }

    public Sistema(Integer siscod) {
        this.siscod = siscod;
    }

    public Sistema(Integer siscod, String sistit, String sisent, String sisdir, String sisruc, BigDecimal sisigv, String sisenc, int sisusr, String sistel, String sisver, int siscolor, String estado, Date feccre, Date fecumv, int usecod, String usenam, String hostname, String siscapPac, int sisalfPac, int sisacfPac, String sistdfPac, String sisdifPac, String siscamInt, int sisnlfVen, String sistarVen, String sisertVen, BigDecimal sisprtVen, String sislrpVen, String sisdvmVen, String sisermVen, BigDecimal sisprmVen, String siserdVen, BigDecimal sisprdVen, String siserhVen, BigDecimal sisprhVen, Date sisirhVen, Date sisfrhVen, String sisecuInv, String sistcuInv, String siscacCli, String sismrmVen, String sisficVen, String sismonS, String sismonD, String sisremCom, String sismdocCaj, String sismodprInv, BigDecimal sisvarprInv, String sisdocopCaj, String siscotstkVen, String sispassfcVen, String sispassctVen, String sisreginfCaj, String sisprescfcVen, String sisprescctVen, String siscarcliVen, BigDecimal sismrdVen, String sismsmVen, String sisvlo, String sisstknegVen, String sistrdtoInv, String sisentTip, String sisdiscoInv, String sisregdtoInv, String sisticom, String sisnewctVen, String sisnewfcVen, String siswsecVen, String sisanuvtaCaj, String sisconimpCaj, String sisvalrucCaj, String sistidvalCaj, String sisdirf, String sismsg1, String sismsg2, String sisprtdfVen, String sisprtdcVen, int sislongrucCaj, String sisredCaj, int sisincenColor, String sisincen, int ndiasanuCaj, String asocdocVen, String sisindstkpedCom, int siscolstkpedCom, String sisalcfacCli, String sisverimptoVen, String sisprodalmInv, String siscagInv, String siscafInv, String siscalInv, String sisdisocCom, String sischkstaVen, String sischkstaCom, String sischkstaInv, String sispasscdInv, int sistactateVen, String sispubfacVen, String siscccom, String sisPreimplot, String sisPreimplotCaj, String sisdoccliCaj, BigDecimal sismontminCaj, String sisreglotfvenInv, String sisactprecosInv, String sistdoccliCaj, String sisviscomfcVen, String sistipredCaj, String sismodvvfocCom, String sisverruc, String sisprdctrctVen, String sisprdctrfcVen, String sisresprdctrdlvVen, BigDecimal sisporcrepentCom, String sisprdnarcfcVen, String sisprdnarcctVen, BigDecimal sismoncredlvCli, String sisanudocuseCaj, String siseditdofacCaj, String sisactconantcateVen, String sismoddatcliVen, String sistipgenface, String sisrsumporcVen, String sisverfvenstaInv, int sisverfvendiasInv, String sisvalperstaInv, String sismoddetcomInv, String sisdlvsta, String sisnocaldtoccnoprgInv, String sisvtatabletVen, String sismsgvalstksecVen, String sistedefIdcalt, String siscodugipres, String sisvalestfaceVen, String sissoldirCaj, String sisstacolestraVen, int siscolorestraVen, String sisstacolprefeVen, int siscolorprefeVen, String sisimpmsjptosCaj, BigDecimal sismaxptosalertCli, String sisidcaltbar, String sisactstaccnoprgInv, String sistdofacVen, String sisreqregmedfcVen, String sisreqregmedctVen, String sisactconantvtasVen, String sisactconantcotiVen, String sissesionsta, String sisdtoadicautVen, String sistipacumptosCli, BigDecimal sismontobasptosCli, BigDecimal siseqvptosCli, String sisappmpCaj, String sissidmpCaj, String sisrestcantpedCom, String sisstasmtpusrpass, String sissmtptencrypt, String sisasiglomayVen, String sismantprecdtocotVen, String sisprecbasVen, String siscalcosordprodInv, BigDecimal sismondifcompInv, int sistimedflentFmg, String sisselautlotInv, int sistimemaxajusfpCaj, String sissmtpmethodaut, String sissmtpcharset, String sissmtpconntype, String sisanunccajcerrVen, String sisanudoccajcerrCaj, long sisminentrDlv, String siscarDlv, String sisstacardCli, BigDecimal sisptosdtoCli, BigDecimal sismoneqvdtoptosCli, BigDecimal sisdtomaxptosCli) {
        this.siscod = siscod;
        this.sistit = sistit;
        this.sisent = sisent;
        this.sisdir = sisdir;
        this.sisruc = sisruc;
        this.sisigv = sisigv;
        this.sisenc = sisenc;
        this.sisusr = sisusr;
        this.sistel = sistel;
        this.sisver = sisver;
        this.siscolor = siscolor;
        this.estado = estado;
        this.feccre = feccre;
        this.fecumv = fecumv;
        this.usecod = usecod;
        this.usenam = usenam;
        this.hostname = hostname;
        this.siscapPac = siscapPac;
        this.sisalfPac = sisalfPac;
        this.sisacfPac = sisacfPac;
        this.sistdfPac = sistdfPac;
        this.sisdifPac = sisdifPac;
        this.siscamInt = siscamInt;
        this.sisnlfVen = sisnlfVen;
        this.sistarVen = sistarVen;
        this.sisertVen = sisertVen;
        this.sisprtVen = sisprtVen;
        this.sislrpVen = sislrpVen;
        this.sisdvmVen = sisdvmVen;
        this.sisermVen = sisermVen;
        this.sisprmVen = sisprmVen;
        this.siserdVen = siserdVen;
        this.sisprdVen = sisprdVen;
        this.siserhVen = siserhVen;
        this.sisprhVen = sisprhVen;
        this.sisirhVen = sisirhVen;
        this.sisfrhVen = sisfrhVen;
        this.sisecuInv = sisecuInv;
        this.sistcuInv = sistcuInv;
        this.siscacCli = siscacCli;
        this.sismrmVen = sismrmVen;
        this.sisficVen = sisficVen;
        this.sismonS = sismonS;
        this.sismonD = sismonD;
        this.sisremCom = sisremCom;
        this.sismdocCaj = sismdocCaj;
        this.sismodprInv = sismodprInv;
        this.sisvarprInv = sisvarprInv;
        this.sisdocopCaj = sisdocopCaj;
        this.siscotstkVen = siscotstkVen;
        this.sispassfcVen = sispassfcVen;
        this.sispassctVen = sispassctVen;
        this.sisreginfCaj = sisreginfCaj;
        this.sisprescfcVen = sisprescfcVen;
        this.sisprescctVen = sisprescctVen;
        this.siscarcliVen = siscarcliVen;
        this.sismrdVen = sismrdVen;
        this.sismsmVen = sismsmVen;
        this.sisvlo = sisvlo;
        this.sisstknegVen = sisstknegVen;
        this.sistrdtoInv = sistrdtoInv;
        this.sisentTip = sisentTip;
        this.sisdiscoInv = sisdiscoInv;
        this.sisregdtoInv = sisregdtoInv;
        this.sisticom = sisticom;
        this.sisnewctVen = sisnewctVen;
        this.sisnewfcVen = sisnewfcVen;
        this.siswsecVen = siswsecVen;
        this.sisanuvtaCaj = sisanuvtaCaj;
        this.sisconimpCaj = sisconimpCaj;
        this.sisvalrucCaj = sisvalrucCaj;
        this.sistidvalCaj = sistidvalCaj;
        this.sisdirf = sisdirf;
        this.sismsg1 = sismsg1;
        this.sismsg2 = sismsg2;
        this.sisprtdfVen = sisprtdfVen;
        this.sisprtdcVen = sisprtdcVen;
        this.sislongrucCaj = sislongrucCaj;
        this.sisredCaj = sisredCaj;
        this.sisincenColor = sisincenColor;
        this.sisincen = sisincen;
        this.ndiasanuCaj = ndiasanuCaj;
        this.asocdocVen = asocdocVen;
        this.sisindstkpedCom = sisindstkpedCom;
        this.siscolstkpedCom = siscolstkpedCom;
        this.sisalcfacCli = sisalcfacCli;
        this.sisverimptoVen = sisverimptoVen;
        this.sisprodalmInv = sisprodalmInv;
        this.siscagInv = siscagInv;
        this.siscafInv = siscafInv;
        this.siscalInv = siscalInv;
        this.sisdisocCom = sisdisocCom;
        this.sischkstaVen = sischkstaVen;
        this.sischkstaCom = sischkstaCom;
        this.sischkstaInv = sischkstaInv;
        this.sispasscdInv = sispasscdInv;
        this.sistactateVen = sistactateVen;
        this.sispubfacVen = sispubfacVen;
        this.siscccom = siscccom;
        this.sisPreimplot = sisPreimplot;
        this.sisPreimplotCaj = sisPreimplotCaj;
        this.sisdoccliCaj = sisdoccliCaj;
        this.sismontminCaj = sismontminCaj;
        this.sisreglotfvenInv = sisreglotfvenInv;
        this.sisactprecosInv = sisactprecosInv;
        this.sistdoccliCaj = sistdoccliCaj;
        this.sisviscomfcVen = sisviscomfcVen;
        this.sistipredCaj = sistipredCaj;
        this.sismodvvfocCom = sismodvvfocCom;
        this.sisverruc = sisverruc;
        this.sisprdctrctVen = sisprdctrctVen;
        this.sisprdctrfcVen = sisprdctrfcVen;
        this.sisresprdctrdlvVen = sisresprdctrdlvVen;
        this.sisporcrepentCom = sisporcrepentCom;
        this.sisprdnarcfcVen = sisprdnarcfcVen;
        this.sisprdnarcctVen = sisprdnarcctVen;
        this.sismoncredlvCli = sismoncredlvCli;
        this.sisanudocuseCaj = sisanudocuseCaj;
        this.siseditdofacCaj = siseditdofacCaj;
        this.sisactconantcateVen = sisactconantcateVen;
        this.sismoddatcliVen = sismoddatcliVen;
        this.sistipgenface = sistipgenface;
        this.sisrsumporcVen = sisrsumporcVen;
        this.sisverfvenstaInv = sisverfvenstaInv;
        this.sisverfvendiasInv = sisverfvendiasInv;
        this.sisvalperstaInv = sisvalperstaInv;
        this.sismoddetcomInv = sismoddetcomInv;
        this.sisdlvsta = sisdlvsta;
        this.sisnocaldtoccnoprgInv = sisnocaldtoccnoprgInv;
        this.sisvtatabletVen = sisvtatabletVen;
        this.sismsgvalstksecVen = sismsgvalstksecVen;
        this.sistedefIdcalt = sistedefIdcalt;
        this.siscodugipres = siscodugipres;
        this.sisvalestfaceVen = sisvalestfaceVen;
        this.sissoldirCaj = sissoldirCaj;
        this.sisstacolestraVen = sisstacolestraVen;
        this.siscolorestraVen = siscolorestraVen;
        this.sisstacolprefeVen = sisstacolprefeVen;
        this.siscolorprefeVen = siscolorprefeVen;
        this.sisimpmsjptosCaj = sisimpmsjptosCaj;
        this.sismaxptosalertCli = sismaxptosalertCli;
        this.sisidcaltbar = sisidcaltbar;
        this.sisactstaccnoprgInv = sisactstaccnoprgInv;
        this.sistdofacVen = sistdofacVen;
        this.sisreqregmedfcVen = sisreqregmedfcVen;
        this.sisreqregmedctVen = sisreqregmedctVen;
        this.sisactconantvtasVen = sisactconantvtasVen;
        this.sisactconantcotiVen = sisactconantcotiVen;
        this.sissesionsta = sissesionsta;
        this.sisdtoadicautVen = sisdtoadicautVen;
        this.sistipacumptosCli = sistipacumptosCli;
        this.sismontobasptosCli = sismontobasptosCli;
        this.siseqvptosCli = siseqvptosCli;
        this.sisappmpCaj = sisappmpCaj;
        this.sissidmpCaj = sissidmpCaj;
        this.sisrestcantpedCom = sisrestcantpedCom;
        this.sisstasmtpusrpass = sisstasmtpusrpass;
        this.sissmtptencrypt = sissmtptencrypt;
        this.sisasiglomayVen = sisasiglomayVen;
        this.sismantprecdtocotVen = sismantprecdtocotVen;
        this.sisprecbasVen = sisprecbasVen;
        this.siscalcosordprodInv = siscalcosordprodInv;
        this.sismondifcompInv = sismondifcompInv;
        this.sistimedflentFmg = sistimedflentFmg;
        this.sisselautlotInv = sisselautlotInv;
        this.sistimemaxajusfpCaj = sistimemaxajusfpCaj;
        this.sissmtpmethodaut = sissmtpmethodaut;
        this.sissmtpcharset = sissmtpcharset;
        this.sissmtpconntype = sissmtpconntype;
        this.sisanunccajcerrVen = sisanunccajcerrVen;
        this.sisanudoccajcerrCaj = sisanudoccajcerrCaj;
        this.sisminentrDlv = sisminentrDlv;
        this.siscarDlv = siscarDlv;
        this.sisstacardCli = sisstacardCli;
        this.sisptosdtoCli = sisptosdtoCli;
        this.sismoneqvdtoptosCli = sismoneqvdtoptosCli;
        this.sisdtomaxptosCli = sisdtomaxptosCli;
    }

    public Integer getSiscod() {
        return siscod;
    }

    public void setSiscod(Integer siscod) {
        this.siscod = siscod;
    }

    public String getSistit() {
        return sistit;
    }

    public void setSistit(String sistit) {
        this.sistit = sistit;
    }

    public String getSisent() {
        return sisent;
    }

    public void setSisent(String sisent) {
        this.sisent = sisent;
    }

    public String getSisdir() {
        return sisdir;
    }

    public void setSisdir(String sisdir) {
        this.sisdir = sisdir;
    }

    public String getSisruc() {
        return sisruc;
    }

    public void setSisruc(String sisruc) {
        this.sisruc = sisruc;
    }

    public BigDecimal getSisigv() {
        return sisigv;
    }

    public void setSisigv(BigDecimal sisigv) {
        this.sisigv = sisigv;
    }

    public String getSisenc() {
        return sisenc;
    }

    public void setSisenc(String sisenc) {
        this.sisenc = sisenc;
    }

    public int getSisusr() {
        return sisusr;
    }

    public void setSisusr(int sisusr) {
        this.sisusr = sisusr;
    }

    public String getSistel() {
        return sistel;
    }

    public void setSistel(String sistel) {
        this.sistel = sistel;
    }

    public String getSisver() {
        return sisver;
    }

    public void setSisver(String sisver) {
        this.sisver = sisver;
    }

    public int getSiscolor() {
        return siscolor;
    }

    public void setSiscolor(int siscolor) {
        this.siscolor = siscolor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFeccre() {
        return feccre;
    }

    public void setFeccre(Date feccre) {
        this.feccre = feccre;
    }

    public Date getFecumv() {
        return fecumv;
    }

    public void setFecumv(Date fecumv) {
        this.fecumv = fecumv;
    }

    public int getUsecod() {
        return usecod;
    }

    public void setUsecod(int usecod) {
        this.usecod = usecod;
    }

    public String getUsenam() {
        return usenam;
    }

    public void setUsenam(String usenam) {
        this.usenam = usenam;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getSiscapPac() {
        return siscapPac;
    }

    public void setSiscapPac(String siscapPac) {
        this.siscapPac = siscapPac;
    }

    public int getSisalfPac() {
        return sisalfPac;
    }

    public void setSisalfPac(int sisalfPac) {
        this.sisalfPac = sisalfPac;
    }

    public int getSisacfPac() {
        return sisacfPac;
    }

    public void setSisacfPac(int sisacfPac) {
        this.sisacfPac = sisacfPac;
    }

    public String getSistdfPac() {
        return sistdfPac;
    }

    public void setSistdfPac(String sistdfPac) {
        this.sistdfPac = sistdfPac;
    }

    public String getSisdifPac() {
        return sisdifPac;
    }

    public void setSisdifPac(String sisdifPac) {
        this.sisdifPac = sisdifPac;
    }

    public String getSiscamInt() {
        return siscamInt;
    }

    public void setSiscamInt(String siscamInt) {
        this.siscamInt = siscamInt;
    }

    public int getSisnlfVen() {
        return sisnlfVen;
    }

    public void setSisnlfVen(int sisnlfVen) {
        this.sisnlfVen = sisnlfVen;
    }

    public String getSistarVen() {
        return sistarVen;
    }

    public void setSistarVen(String sistarVen) {
        this.sistarVen = sistarVen;
    }

    public String getSisertVen() {
        return sisertVen;
    }

    public void setSisertVen(String sisertVen) {
        this.sisertVen = sisertVen;
    }

    public BigDecimal getSisprtVen() {
        return sisprtVen;
    }

    public void setSisprtVen(BigDecimal sisprtVen) {
        this.sisprtVen = sisprtVen;
    }

    public String getSislrpVen() {
        return sislrpVen;
    }

    public void setSislrpVen(String sislrpVen) {
        this.sislrpVen = sislrpVen;
    }

    public String getSisdvmVen() {
        return sisdvmVen;
    }

    public void setSisdvmVen(String sisdvmVen) {
        this.sisdvmVen = sisdvmVen;
    }

    public String getSisermVen() {
        return sisermVen;
    }

    public void setSisermVen(String sisermVen) {
        this.sisermVen = sisermVen;
    }

    public BigDecimal getSisprmVen() {
        return sisprmVen;
    }

    public void setSisprmVen(BigDecimal sisprmVen) {
        this.sisprmVen = sisprmVen;
    }

    public String getSiserdVen() {
        return siserdVen;
    }

    public void setSiserdVen(String siserdVen) {
        this.siserdVen = siserdVen;
    }

    public BigDecimal getSisprdVen() {
        return sisprdVen;
    }

    public void setSisprdVen(BigDecimal sisprdVen) {
        this.sisprdVen = sisprdVen;
    }

    public String getSiserhVen() {
        return siserhVen;
    }

    public void setSiserhVen(String siserhVen) {
        this.siserhVen = siserhVen;
    }

    public BigDecimal getSisprhVen() {
        return sisprhVen;
    }

    public void setSisprhVen(BigDecimal sisprhVen) {
        this.sisprhVen = sisprhVen;
    }

    public Date getSisirhVen() {
        return sisirhVen;
    }

    public void setSisirhVen(Date sisirhVen) {
        this.sisirhVen = sisirhVen;
    }

    public Date getSisfrhVen() {
        return sisfrhVen;
    }

    public void setSisfrhVen(Date sisfrhVen) {
        this.sisfrhVen = sisfrhVen;
    }

    public String getSisecuInv() {
        return sisecuInv;
    }

    public void setSisecuInv(String sisecuInv) {
        this.sisecuInv = sisecuInv;
    }

    public String getSistcuInv() {
        return sistcuInv;
    }

    public void setSistcuInv(String sistcuInv) {
        this.sistcuInv = sistcuInv;
    }

    public String getSiscacCli() {
        return siscacCli;
    }

    public void setSiscacCli(String siscacCli) {
        this.siscacCli = siscacCli;
    }

    public String getSismrmVen() {
        return sismrmVen;
    }

    public void setSismrmVen(String sismrmVen) {
        this.sismrmVen = sismrmVen;
    }

    public String getSisficVen() {
        return sisficVen;
    }

    public void setSisficVen(String sisficVen) {
        this.sisficVen = sisficVen;
    }

    public String getSismonS() {
        return sismonS;
    }

    public void setSismonS(String sismonS) {
        this.sismonS = sismonS;
    }

    public String getSismonD() {
        return sismonD;
    }

    public void setSismonD(String sismonD) {
        this.sismonD = sismonD;
    }

    public String getSispathUpdate() {
        return sispathUpdate;
    }

    public void setSispathUpdate(String sispathUpdate) {
        this.sispathUpdate = sispathUpdate;
    }

    public String getSisremCom() {
        return sisremCom;
    }

    public void setSisremCom(String sisremCom) {
        this.sisremCom = sisremCom;
    }

    public String getSismdocCaj() {
        return sismdocCaj;
    }

    public void setSismdocCaj(String sismdocCaj) {
        this.sismdocCaj = sismdocCaj;
    }

    public String getSismodprInv() {
        return sismodprInv;
    }

    public void setSismodprInv(String sismodprInv) {
        this.sismodprInv = sismodprInv;
    }

    public BigDecimal getSisvarprInv() {
        return sisvarprInv;
    }

    public void setSisvarprInv(BigDecimal sisvarprInv) {
        this.sisvarprInv = sisvarprInv;
    }

    public String getSisdocopCaj() {
        return sisdocopCaj;
    }

    public void setSisdocopCaj(String sisdocopCaj) {
        this.sisdocopCaj = sisdocopCaj;
    }

    public String getSiscotstkVen() {
        return siscotstkVen;
    }

    public void setSiscotstkVen(String siscotstkVen) {
        this.siscotstkVen = siscotstkVen;
    }

    public String getSispassfcVen() {
        return sispassfcVen;
    }

    public void setSispassfcVen(String sispassfcVen) {
        this.sispassfcVen = sispassfcVen;
    }

    public String getSispassctVen() {
        return sispassctVen;
    }

    public void setSispassctVen(String sispassctVen) {
        this.sispassctVen = sispassctVen;
    }

    public String getSisreginfCaj() {
        return sisreginfCaj;
    }

    public void setSisreginfCaj(String sisreginfCaj) {
        this.sisreginfCaj = sisreginfCaj;
    }

    public String getSisprescfcVen() {
        return sisprescfcVen;
    }

    public void setSisprescfcVen(String sisprescfcVen) {
        this.sisprescfcVen = sisprescfcVen;
    }

    public String getSisprescctVen() {
        return sisprescctVen;
    }

    public void setSisprescctVen(String sisprescctVen) {
        this.sisprescctVen = sisprescctVen;
    }

    public String getSiscarcliVen() {
        return siscarcliVen;
    }

    public void setSiscarcliVen(String siscarcliVen) {
        this.siscarcliVen = siscarcliVen;
    }

    public BigDecimal getSismrdVen() {
        return sismrdVen;
    }

    public void setSismrdVen(BigDecimal sismrdVen) {
        this.sismrdVen = sismrdVen;
    }

    public String getSismsmVen() {
        return sismsmVen;
    }

    public void setSismsmVen(String sismsmVen) {
        this.sismsmVen = sismsmVen;
    }

    public String getSisvlo() {
        return sisvlo;
    }

    public void setSisvlo(String sisvlo) {
        this.sisvlo = sisvlo;
    }

    public String getSisstknegVen() {
        return sisstknegVen;
    }

    public void setSisstknegVen(String sisstknegVen) {
        this.sisstknegVen = sisstknegVen;
    }

    public String getSistrdtoInv() {
        return sistrdtoInv;
    }

    public void setSistrdtoInv(String sistrdtoInv) {
        this.sistrdtoInv = sistrdtoInv;
    }

    public String getSisentTip() {
        return sisentTip;
    }

    public void setSisentTip(String sisentTip) {
        this.sisentTip = sisentTip;
    }

    public String getSisdiscoInv() {
        return sisdiscoInv;
    }

    public void setSisdiscoInv(String sisdiscoInv) {
        this.sisdiscoInv = sisdiscoInv;
    }

    public String getSisregdtoInv() {
        return sisregdtoInv;
    }

    public void setSisregdtoInv(String sisregdtoInv) {
        this.sisregdtoInv = sisregdtoInv;
    }

    public String getSisticom() {
        return sisticom;
    }

    public void setSisticom(String sisticom) {
        this.sisticom = sisticom;
    }

    public String getSismail() {
        return sismail;
    }

    public void setSismail(String sismail) {
        this.sismail = sismail;
    }

    public String getSisnewctVen() {
        return sisnewctVen;
    }

    public void setSisnewctVen(String sisnewctVen) {
        this.sisnewctVen = sisnewctVen;
    }

    public String getSisnewfcVen() {
        return sisnewfcVen;
    }

    public void setSisnewfcVen(String sisnewfcVen) {
        this.sisnewfcVen = sisnewfcVen;
    }

    public String getSiswsecVen() {
        return siswsecVen;
    }

    public void setSiswsecVen(String siswsecVen) {
        this.siswsecVen = siswsecVen;
    }

    public String getSisanuvtaCaj() {
        return sisanuvtaCaj;
    }

    public void setSisanuvtaCaj(String sisanuvtaCaj) {
        this.sisanuvtaCaj = sisanuvtaCaj;
    }

    public String getSisconimpCaj() {
        return sisconimpCaj;
    }

    public void setSisconimpCaj(String sisconimpCaj) {
        this.sisconimpCaj = sisconimpCaj;
    }

    public String getSisvalrucCaj() {
        return sisvalrucCaj;
    }

    public void setSisvalrucCaj(String sisvalrucCaj) {
        this.sisvalrucCaj = sisvalrucCaj;
    }

    public String getSistidvalCaj() {
        return sistidvalCaj;
    }

    public void setSistidvalCaj(String sistidvalCaj) {
        this.sistidvalCaj = sistidvalCaj;
    }

    public String getSisdirf() {
        return sisdirf;
    }

    public void setSisdirf(String sisdirf) {
        this.sisdirf = sisdirf;
    }

    public String getSismsg1() {
        return sismsg1;
    }

    public void setSismsg1(String sismsg1) {
        this.sismsg1 = sismsg1;
    }

    public String getSismsg2() {
        return sismsg2;
    }

    public void setSismsg2(String sismsg2) {
        this.sismsg2 = sismsg2;
    }

    public String getSisalmtrInv() {
        return sisalmtrInv;
    }

    public void setSisalmtrInv(String sisalmtrInv) {
        this.sisalmtrInv = sisalmtrInv;
    }

    public String getSisprtdfVen() {
        return sisprtdfVen;
    }

    public void setSisprtdfVen(String sisprtdfVen) {
        this.sisprtdfVen = sisprtdfVen;
    }

    public String getSisprtdcVen() {
        return sisprtdcVen;
    }

    public void setSisprtdcVen(String sisprtdcVen) {
        this.sisprtdcVen = sisprtdcVen;
    }

    public int getSislongrucCaj() {
        return sislongrucCaj;
    }

    public void setSislongrucCaj(int sislongrucCaj) {
        this.sislongrucCaj = sislongrucCaj;
    }

    public String getSisredCaj() {
        return sisredCaj;
    }

    public void setSisredCaj(String sisredCaj) {
        this.sisredCaj = sisredCaj;
    }

    public int getSisincenColor() {
        return sisincenColor;
    }

    public void setSisincenColor(int sisincenColor) {
        this.sisincenColor = sisincenColor;
    }

    public String getSisincen() {
        return sisincen;
    }

    public void setSisincen(String sisincen) {
        this.sisincen = sisincen;
    }

    public String getGruncod() {
        return gruncod;
    }

    public void setGruncod(String gruncod) {
        this.gruncod = gruncod;
    }

    public int getNdiasanuCaj() {
        return ndiasanuCaj;
    }

    public void setNdiasanuCaj(int ndiasanuCaj) {
        this.ndiasanuCaj = ndiasanuCaj;
    }

    public String getAsocdocVen() {
        return asocdocVen;
    }

    public void setAsocdocVen(String asocdocVen) {
        this.asocdocVen = asocdocVen;
    }

    public String getTdoserVen() {
        return tdoserVen;
    }

    public void setTdoserVen(String tdoserVen) {
        this.tdoserVen = tdoserVen;
    }

    public String getSisindstkpedCom() {
        return sisindstkpedCom;
    }

    public void setSisindstkpedCom(String sisindstkpedCom) {
        this.sisindstkpedCom = sisindstkpedCom;
    }

    public int getSiscolstkpedCom() {
        return siscolstkpedCom;
    }

    public void setSiscolstkpedCom(int siscolstkpedCom) {
        this.siscolstkpedCom = siscolstkpedCom;
    }

    public String getSisalcfacCli() {
        return sisalcfacCli;
    }

    public void setSisalcfacCli(String sisalcfacCli) {
        this.sisalcfacCli = sisalcfacCli;
    }

    public String getSisverimptoVen() {
        return sisverimptoVen;
    }

    public void setSisverimptoVen(String sisverimptoVen) {
        this.sisverimptoVen = sisverimptoVen;
    }

    public String getSisprodalmInv() {
        return sisprodalmInv;
    }

    public void setSisprodalmInv(String sisprodalmInv) {
        this.sisprodalmInv = sisprodalmInv;
    }

    public String getSiscagInv() {
        return siscagInv;
    }

    public void setSiscagInv(String siscagInv) {
        this.siscagInv = siscagInv;
    }

    public String getSiscafInv() {
        return siscafInv;
    }

    public void setSiscafInv(String siscafInv) {
        this.siscafInv = siscafInv;
    }

    public String getSiscalInv() {
        return siscalInv;
    }

    public void setSiscalInv(String siscalInv) {
        this.siscalInv = siscalInv;
    }

    public String getSisdisocCom() {
        return sisdisocCom;
    }

    public void setSisdisocCom(String sisdisocCom) {
        this.sisdisocCom = sisdisocCom;
    }

    public String getSischkstaVen() {
        return sischkstaVen;
    }

    public void setSischkstaVen(String sischkstaVen) {
        this.sischkstaVen = sischkstaVen;
    }

    public Integer getSischkcodVen() {
        return sischkcodVen;
    }

    public void setSischkcodVen(Integer sischkcodVen) {
        this.sischkcodVen = sischkcodVen;
    }

    public String getSischkstaCom() {
        return sischkstaCom;
    }

    public void setSischkstaCom(String sischkstaCom) {
        this.sischkstaCom = sischkstaCom;
    }

    public Integer getSischkcodCom() {
        return sischkcodCom;
    }

    public void setSischkcodCom(Integer sischkcodCom) {
        this.sischkcodCom = sischkcodCom;
    }

    public String getSischkstaInv() {
        return sischkstaInv;
    }

    public void setSischkstaInv(String sischkstaInv) {
        this.sischkstaInv = sischkstaInv;
    }

    public Integer getSischkcodInv() {
        return sischkcodInv;
    }

    public void setSischkcodInv(Integer sischkcodInv) {
        this.sischkcodInv = sischkcodInv;
    }

    public String getSispasscdInv() {
        return sispasscdInv;
    }

    public void setSispasscdInv(String sispasscdInv) {
        this.sispasscdInv = sispasscdInv;
    }

    public int getSistactateVen() {
        return sistactateVen;
    }

    public void setSistactateVen(int sistactateVen) {
        this.sistactateVen = sistactateVen;
    }

    public String getSispubfacVen() {
        return sispubfacVen;
    }

    public void setSispubfacVen(String sispubfacVen) {
        this.sispubfacVen = sispubfacVen;
    }

    public String getSissecCommit() {
        return sissecCommit;
    }

    public void setSissecCommit(String sissecCommit) {
        this.sissecCommit = sissecCommit;
    }

    public String getSiscccom() {
        return siscccom;
    }

    public void setSiscccom(String siscccom) {
        this.siscccom = siscccom;
    }

    public String getSiscomordcomCom() {
        return siscomordcomCom;
    }

    public void setSiscomordcomCom(String siscomordcomCom) {
        this.siscomordcomCom = siscomordcomCom;
    }

    public String getSisPreimplot() {
        return sisPreimplot;
    }

    public void setSisPreimplot(String sisPreimplot) {
        this.sisPreimplot = sisPreimplot;
    }

    public String getSisPreimplotCaj() {
        return sisPreimplotCaj;
    }

    public void setSisPreimplotCaj(String sisPreimplotCaj) {
        this.sisPreimplotCaj = sisPreimplotCaj;
    }

    public String getSisdoccliCaj() {
        return sisdoccliCaj;
    }

    public void setSisdoccliCaj(String sisdoccliCaj) {
        this.sisdoccliCaj = sisdoccliCaj;
    }

    public BigDecimal getSismontminCaj() {
        return sismontminCaj;
    }

    public void setSismontminCaj(BigDecimal sismontminCaj) {
        this.sismontminCaj = sismontminCaj;
    }

    public String getSisreglotfvenInv() {
        return sisreglotfvenInv;
    }

    public void setSisreglotfvenInv(String sisreglotfvenInv) {
        this.sisreglotfvenInv = sisreglotfvenInv;
    }

    public String getSisactprecosInv() {
        return sisactprecosInv;
    }

    public void setSisactprecosInv(String sisactprecosInv) {
        this.sisactprecosInv = sisactprecosInv;
    }

    public String getSistdoccliCaj() {
        return sistdoccliCaj;
    }

    public void setSistdoccliCaj(String sistdoccliCaj) {
        this.sistdoccliCaj = sistdoccliCaj;
    }

    public String getSisviscomfcVen() {
        return sisviscomfcVen;
    }

    public void setSisviscomfcVen(String sisviscomfcVen) {
        this.sisviscomfcVen = sisviscomfcVen;
    }

    public String getSisfileprescPre() {
        return sisfileprescPre;
    }

    public void setSisfileprescPre(String sisfileprescPre) {
        this.sisfileprescPre = sisfileprescPre;
    }

    public String getSistipredCaj() {
        return sistipredCaj;
    }

    public void setSistipredCaj(String sistipredCaj) {
        this.sistipredCaj = sistipredCaj;
    }

    public String getSismodvvfocCom() {
        return sismodvvfocCom;
    }

    public void setSismodvvfocCom(String sismodvvfocCom) {
        this.sismodvvfocCom = sismodvvfocCom;
    }

    public String getSisverruc() {
        return sisverruc;
    }

    public void setSisverruc(String sisverruc) {
        this.sisverruc = sisverruc;
    }

    public String getSisprdctrctVen() {
        return sisprdctrctVen;
    }

    public void setSisprdctrctVen(String sisprdctrctVen) {
        this.sisprdctrctVen = sisprdctrctVen;
    }

    public String getSisprdctrfcVen() {
        return sisprdctrfcVen;
    }

    public void setSisprdctrfcVen(String sisprdctrfcVen) {
        this.sisprdctrfcVen = sisprdctrfcVen;
    }

    public String getSisresprdctrdlvVen() {
        return sisresprdctrdlvVen;
    }

    public void setSisresprdctrdlvVen(String sisresprdctrdlvVen) {
        this.sisresprdctrdlvVen = sisresprdctrdlvVen;
    }

    public BigDecimal getSisporcrepentCom() {
        return sisporcrepentCom;
    }

    public void setSisporcrepentCom(BigDecimal sisporcrepentCom) {
        this.sisporcrepentCom = sisporcrepentCom;
    }

    public String getSisprdnarcfcVen() {
        return sisprdnarcfcVen;
    }

    public void setSisprdnarcfcVen(String sisprdnarcfcVen) {
        this.sisprdnarcfcVen = sisprdnarcfcVen;
    }

    public String getSisprdnarcctVen() {
        return sisprdnarcctVen;
    }

    public void setSisprdnarcctVen(String sisprdnarcctVen) {
        this.sisprdnarcctVen = sisprdnarcctVen;
    }

    public BigDecimal getSismoncredlvCli() {
        return sismoncredlvCli;
    }

    public void setSismoncredlvCli(BigDecimal sismoncredlvCli) {
        this.sismoncredlvCli = sismoncredlvCli;
    }

    public Date getSisfficredlvCli() {
        return sisfficredlvCli;
    }

    public void setSisfficredlvCli(Date sisfficredlvCli) {
        this.sisfficredlvCli = sisfficredlvCli;
    }

    public String getSiscodpais() {
        return siscodpais;
    }

    public void setSiscodpais(String siscodpais) {
        this.siscodpais = siscodpais;
    }

    public String getSislocalidad() {
        return sislocalidad;
    }

    public void setSislocalidad(String sislocalidad) {
        this.sislocalidad = sislocalidad;
    }

    public String getSisanudocuseCaj() {
        return sisanudocuseCaj;
    }

    public void setSisanudocuseCaj(String sisanudocuseCaj) {
        this.sisanudocuseCaj = sisanudocuseCaj;
    }

    public String getSiseditdofacCaj() {
        return siseditdofacCaj;
    }

    public void setSiseditdofacCaj(String siseditdofacCaj) {
        this.siseditdofacCaj = siseditdofacCaj;
    }

    public String getSisactconantcateVen() {
        return sisactconantcateVen;
    }

    public void setSisactconantcateVen(String sisactconantcateVen) {
        this.sisactconantcateVen = sisactconantcateVen;
    }

    public String getSismoddatcliVen() {
        return sismoddatcliVen;
    }

    public void setSismoddatcliVen(String sismoddatcliVen) {
        this.sismoddatcliVen = sismoddatcliVen;
    }

    public String getSistipgenface() {
        return sistipgenface;
    }

    public void setSistipgenface(String sistipgenface) {
        this.sistipgenface = sistipgenface;
    }

    public String getSisrsumporcVen() {
        return sisrsumporcVen;
    }

    public void setSisrsumporcVen(String sisrsumporcVen) {
        this.sisrsumporcVen = sisrsumporcVen;
    }

    public String getSisverfvenstaInv() {
        return sisverfvenstaInv;
    }

    public void setSisverfvenstaInv(String sisverfvenstaInv) {
        this.sisverfvenstaInv = sisverfvenstaInv;
    }

    public int getSisverfvendiasInv() {
        return sisverfvendiasInv;
    }

    public void setSisverfvendiasInv(int sisverfvendiasInv) {
        this.sisverfvendiasInv = sisverfvendiasInv;
    }

    public String getSisvalperstaInv() {
        return sisvalperstaInv;
    }

    public void setSisvalperstaInv(String sisvalperstaInv) {
        this.sisvalperstaInv = sisvalperstaInv;
    }

    public String getSismoddetcomInv() {
        return sismoddetcomInv;
    }

    public void setSismoddetcomInv(String sismoddetcomInv) {
        this.sismoddetcomInv = sismoddetcomInv;
    }

    public String getSisresface() {
        return sisresface;
    }

    public void setSisresface(String sisresface) {
        this.sisresface = sisresface;
    }

    public String getSisurlface() {
        return sisurlface;
    }

    public void setSisurlface(String sisurlface) {
        this.sisurlface = sisurlface;
    }

    public String getSislogo() {
        return sislogo;
    }

    public void setSislogo(String sislogo) {
        this.sislogo = sislogo;
    }

    public String getSismsg3() {
        return sismsg3;
    }

    public void setSismsg3(String sismsg3) {
        this.sismsg3 = sismsg3;
    }

    public String getSisdlvsta() {
        return sisdlvsta;
    }

    public void setSisdlvsta(String sisdlvsta) {
        this.sisdlvsta = sisdlvsta;
    }

    public String getSisnocaldtoccnoprgInv() {
        return sisnocaldtoccnoprgInv;
    }

    public void setSisnocaldtoccnoprgInv(String sisnocaldtoccnoprgInv) {
        this.sisnocaldtoccnoprgInv = sisnocaldtoccnoprgInv;
    }

    public String getSisvtatabletVen() {
        return sisvtatabletVen;
    }

    public void setSisvtatabletVen(String sisvtatabletVen) {
        this.sisvtatabletVen = sisvtatabletVen;
    }

    public String getSismsgvalstksecVen() {
        return sismsgvalstksecVen;
    }

    public void setSismsgvalstksecVen(String sismsgvalstksecVen) {
        this.sismsgvalstksecVen = sismsgvalstksecVen;
    }

    public String getSistedefIdcalt() {
        return sistedefIdcalt;
    }

    public void setSistedefIdcalt(String sistedefIdcalt) {
        this.sistedefIdcalt = sistedefIdcalt;
    }

    public String getSisfiletedefLeg() {
        return sisfiletedefLeg;
    }

    public void setSisfiletedefLeg(String sisfiletedefLeg) {
        this.sisfiletedefLeg = sisfiletedefLeg;
    }

    public String getSiscodugipres() {
        return siscodugipres;
    }

    public void setSiscodugipres(String siscodugipres) {
        this.siscodugipres = siscodugipres;
    }

    public String getSisnamface() {
        return sisnamface;
    }

    public void setSisnamface(String sisnamface) {
        this.sisnamface = sisnamface;
    }

    public String getSispasface() {
        return sispasface;
    }

    public void setSispasface(String sispasface) {
        this.sispasface = sispasface;
    }

    public String getSisvalestfaceVen() {
        return sisvalestfaceVen;
    }

    public void setSisvalestfaceVen(String sisvalestfaceVen) {
        this.sisvalestfaceVen = sisvalestfaceVen;
    }

    public String getSissoldirCaj() {
        return sissoldirCaj;
    }

    public void setSissoldirCaj(String sissoldirCaj) {
        this.sissoldirCaj = sissoldirCaj;
    }

    public String getSistdofacCaj() {
        return sistdofacCaj;
    }

    public void setSistdofacCaj(String sistdofacCaj) {
        this.sistdofacCaj = sistdofacCaj;
    }

    public String getSisstacolestraVen() {
        return sisstacolestraVen;
    }

    public void setSisstacolestraVen(String sisstacolestraVen) {
        this.sisstacolestraVen = sisstacolestraVen;
    }

    public int getSiscolorestraVen() {
        return siscolorestraVen;
    }

    public void setSiscolorestraVen(int siscolorestraVen) {
        this.siscolorestraVen = siscolorestraVen;
    }

    public String getSisstacolprefeVen() {
        return sisstacolprefeVen;
    }

    public void setSisstacolprefeVen(String sisstacolprefeVen) {
        this.sisstacolprefeVen = sisstacolprefeVen;
    }

    public int getSiscolorprefeVen() {
        return siscolorprefeVen;
    }

    public void setSiscolorprefeVen(int siscolorprefeVen) {
        this.siscolorprefeVen = siscolorprefeVen;
    }

    public String getSisimpmsjptosCaj() {
        return sisimpmsjptosCaj;
    }

    public void setSisimpmsjptosCaj(String sisimpmsjptosCaj) {
        this.sisimpmsjptosCaj = sisimpmsjptosCaj;
    }

    public BigDecimal getSismaxptosalertCli() {
        return sismaxptosalertCli;
    }

    public void setSismaxptosalertCli(BigDecimal sismaxptosalertCli) {
        this.sismaxptosalertCli = sismaxptosalertCli;
    }

    public String getSisidcaltbar() {
        return sisidcaltbar;
    }

    public void setSisidcaltbar(String sisidcaltbar) {
        this.sisidcaltbar = sisidcaltbar;
    }

    public String getSisactstaccnoprgInv() {
        return sisactstaccnoprgInv;
    }

    public void setSisactstaccnoprgInv(String sisactstaccnoprgInv) {
        this.sisactstaccnoprgInv = sisactstaccnoprgInv;
    }

    public String getSispathSited() {
        return sispathSited;
    }

    public void setSispathSited(String sispathSited) {
        this.sispathSited = sispathSited;
    }

    public String getSiscodipress() {
        return siscodipress;
    }

    public void setSiscodipress(String siscodipress) {
        this.siscodipress = siscodipress;
    }

    public String getSisuseripress() {
        return sisuseripress;
    }

    public void setSisuseripress(String sisuseripress) {
        this.sisuseripress = sisuseripress;
    }

    public String getSispasipress() {
        return sispasipress;
    }

    public void setSispasipress(String sispasipress) {
        this.sispasipress = sispasipress;
    }

    public String getSistdofacVen() {
        return sistdofacVen;
    }

    public void setSistdofacVen(String sistdofacVen) {
        this.sistdofacVen = sistdofacVen;
    }

    public String getSisreqregmedfcVen() {
        return sisreqregmedfcVen;
    }

    public void setSisreqregmedfcVen(String sisreqregmedfcVen) {
        this.sisreqregmedfcVen = sisreqregmedfcVen;
    }

    public String getSisreqregmedctVen() {
        return sisreqregmedctVen;
    }

    public void setSisreqregmedctVen(String sisreqregmedctVen) {
        this.sisreqregmedctVen = sisreqregmedctVen;
    }

    public String getSisactconantvtasVen() {
        return sisactconantvtasVen;
    }

    public void setSisactconantvtasVen(String sisactconantvtasVen) {
        this.sisactconantvtasVen = sisactconantvtasVen;
    }

    public String getSisactconantcotiVen() {
        return sisactconantcotiVen;
    }

    public void setSisactconantcotiVen(String sisactconantcotiVen) {
        this.sisactconantcotiVen = sisactconantcotiVen;
    }

    public String getSissesionsta() {
        return sissesionsta;
    }

    public void setSissesionsta(String sissesionsta) {
        this.sissesionsta = sissesionsta;
    }

    public String getSisdtoadicautVen() {
        return sisdtoadicautVen;
    }

    public void setSisdtoadicautVen(String sisdtoadicautVen) {
        this.sisdtoadicautVen = sisdtoadicautVen;
    }

    public String getSistipacumptosCli() {
        return sistipacumptosCli;
    }

    public void setSistipacumptosCli(String sistipacumptosCli) {
        this.sistipacumptosCli = sistipacumptosCli;
    }

    public BigDecimal getSismontobasptosCli() {
        return sismontobasptosCli;
    }

    public void setSismontobasptosCli(BigDecimal sismontobasptosCli) {
        this.sismontobasptosCli = sismontobasptosCli;
    }

    public BigDecimal getSiseqvptosCli() {
        return siseqvptosCli;
    }

    public void setSiseqvptosCli(BigDecimal siseqvptosCli) {
        this.siseqvptosCli = siseqvptosCli;
    }

    public String getCodalmqullfarVen() {
        return codalmqullfarVen;
    }

    public void setCodalmqullfarVen(String codalmqullfarVen) {
        this.codalmqullfarVen = codalmqullfarVen;
    }

    public Integer getUsecodqullfarVen() {
        return usecodqullfarVen;
    }

    public void setUsecodqullfarVen(Integer usecodqullfarVen) {
        this.usecodqullfarVen = usecodqullfarVen;
    }

    public String getUrlimgrecqullfarVen() {
        return urlimgrecqullfarVen;
    }

    public void setUrlimgrecqullfarVen(String urlimgrecqullfarVen) {
        this.urlimgrecqullfarVen = urlimgrecqullfarVen;
    }

    public String getSisappmpCaj() {
        return sisappmpCaj;
    }

    public void setSisappmpCaj(String sisappmpCaj) {
        this.sisappmpCaj = sisappmpCaj;
    }

    public String getSissidmpCaj() {
        return sissidmpCaj;
    }

    public void setSissidmpCaj(String sissidmpCaj) {
        this.sissidmpCaj = sissidmpCaj;
    }

    public String getSisrestcantpedCom() {
        return sisrestcantpedCom;
    }

    public void setSisrestcantpedCom(String sisrestcantpedCom) {
        this.sisrestcantpedCom = sisrestcantpedCom;
    }

    public String getSissmtpserver() {
        return sissmtpserver;
    }

    public void setSissmtpserver(String sissmtpserver) {
        this.sissmtpserver = sissmtpserver;
    }

    public String getSissmtpport() {
        return sissmtpport;
    }

    public void setSissmtpport(String sissmtpport) {
        this.sissmtpport = sissmtpport;
    }

    public String getSissmtpuserid() {
        return sissmtpuserid;
    }

    public void setSissmtpuserid(String sissmtpuserid) {
        this.sissmtpuserid = sissmtpuserid;
    }

    public String getSissmtppass() {
        return sissmtppass;
    }

    public void setSissmtppass(String sissmtppass) {
        this.sissmtppass = sissmtppass;
    }

    public String getSisstasmtpusrpass() {
        return sisstasmtpusrpass;
    }

    public void setSisstasmtpusrpass(String sisstasmtpusrpass) {
        this.sisstasmtpusrpass = sisstasmtpusrpass;
    }

    public String getSissmtptencrypt() {
        return sissmtptencrypt;
    }

    public void setSissmtptencrypt(String sissmtptencrypt) {
        this.sissmtptencrypt = sissmtptencrypt;
    }

    public String getSisasiglomayVen() {
        return sisasiglomayVen;
    }

    public void setSisasiglomayVen(String sisasiglomayVen) {
        this.sisasiglomayVen = sisasiglomayVen;
    }

    public String getSismantprecdtocotVen() {
        return sismantprecdtocotVen;
    }

    public void setSismantprecdtocotVen(String sismantprecdtocotVen) {
        this.sismantprecdtocotVen = sismantprecdtocotVen;
    }

    public String getSismsgcotVen() {
        return sismsgcotVen;
    }

    public void setSismsgcotVen(String sismsgcotVen) {
        this.sismsgcotVen = sismsgcotVen;
    }

    public String getSismsgadicCaj() {
        return sismsgadicCaj;
    }

    public void setSismsgadicCaj(String sismsgadicCaj) {
        this.sismsgadicCaj = sismsgadicCaj;
    }

    public String getSisctaenvocCom() {
        return sisctaenvocCom;
    }

    public void setSisctaenvocCom(String sisctaenvocCom) {
        this.sisctaenvocCom = sisctaenvocCom;
    }

    public String getSispathpdflocCom() {
        return sispathpdflocCom;
    }

    public void setSispathpdflocCom(String sispathpdflocCom) {
        this.sispathpdflocCom = sispathpdflocCom;
    }

    public String getSistextenvocCom() {
        return sistextenvocCom;
    }

    public void setSistextenvocCom(String sistextenvocCom) {
        this.sistextenvocCom = sistextenvocCom;
    }

    public String getSiscodaltbarrInv() {
        return siscodaltbarrInv;
    }

    public void setSiscodaltbarrInv(String siscodaltbarrInv) {
        this.siscodaltbarrInv = siscodaltbarrInv;
    }

    public String getSisprecbasVen() {
        return sisprecbasVen;
    }

    public void setSisprecbasVen(String sisprecbasVen) {
        this.sisprecbasVen = sisprecbasVen;
    }

    public String getSiscodalminsFmg() {
        return siscodalminsFmg;
    }

    public void setSiscodalminsFmg(String siscodalminsFmg) {
        this.siscodalminsFmg = siscodalminsFmg;
    }

    public String getSiscalcosordprodInv() {
        return siscalcosordprodInv;
    }

    public void setSiscalcosordprodInv(String siscalcosordprodInv) {
        this.siscalcosordprodInv = siscalcosordprodInv;
    }

    public BigDecimal getSismondifcompInv() {
        return sismondifcompInv;
    }

    public void setSismondifcompInv(BigDecimal sismondifcompInv) {
        this.sismondifcompInv = sismondifcompInv;
    }

    public int getSistimedflentFmg() {
        return sistimedflentFmg;
    }

    public void setSistimedflentFmg(int sistimedflentFmg) {
        this.sistimedflentFmg = sistimedflentFmg;
    }

    public String getSiscodundodsFmg() {
        return siscodundodsFmg;
    }

    public void setSiscodundodsFmg(String siscodundodsFmg) {
        this.siscodundodsFmg = siscodundodsFmg;
    }

    public String getSiscodtprepodsFmg() {
        return siscodtprepodsFmg;
    }

    public void setSiscodtprepodsFmg(String siscodtprepodsFmg) {
        this.siscodtprepodsFmg = siscodtprepodsFmg;
    }

    public String getSisselautlotInv() {
        return sisselautlotInv;
    }

    public void setSisselautlotInv(String sisselautlotInv) {
        this.sisselautlotInv = sisselautlotInv;
    }

    public int getSistimemaxajusfpCaj() {
        return sistimemaxajusfpCaj;
    }

    public void setSistimemaxajusfpCaj(int sistimemaxajusfpCaj) {
        this.sistimemaxajusfpCaj = sistimemaxajusfpCaj;
    }

    public String getSissmtpmethodaut() {
        return sissmtpmethodaut;
    }

    public void setSissmtpmethodaut(String sissmtpmethodaut) {
        this.sissmtpmethodaut = sissmtpmethodaut;
    }

    public String getSissmtpcharset() {
        return sissmtpcharset;
    }

    public void setSissmtpcharset(String sissmtpcharset) {
        this.sissmtpcharset = sissmtpcharset;
    }

    public String getSissmtpconntype() {
        return sissmtpconntype;
    }

    public void setSissmtpconntype(String sissmtpconntype) {
        this.sissmtpconntype = sissmtpconntype;
    }

    public String getSisfilelibeLeg() {
        return sisfilelibeLeg;
    }

    public void setSisfilelibeLeg(String sisfilelibeLeg) {
        this.sisfilelibeLeg = sisfilelibeLeg;
    }

    public String getSiscodtri() {
        return siscodtri;
    }

    public void setSiscodtri(String siscodtri) {
        this.siscodtri = siscodtri;
    }

    public String getSisanunccajcerrVen() {
        return sisanunccajcerrVen;
    }

    public void setSisanunccajcerrVen(String sisanunccajcerrVen) {
        this.sisanunccajcerrVen = sisanunccajcerrVen;
    }

    public String getSisanudoccajcerrCaj() {
        return sisanudoccajcerrCaj;
    }

    public void setSisanudoccajcerrCaj(String sisanudoccajcerrCaj) {
        this.sisanudoccajcerrCaj = sisanudoccajcerrCaj;
    }

    public long getSisminentrDlv() {
        return sisminentrDlv;
    }

    public void setSisminentrDlv(long sisminentrDlv) {
        this.sisminentrDlv = sisminentrDlv;
    }

    public Date getSishinientrDlv() {
        return sishinientrDlv;
    }

    public void setSishinientrDlv(Date sishinientrDlv) {
        this.sishinientrDlv = sishinientrDlv;
    }

    public Date getSishfinentrDlv() {
        return sishfinentrDlv;
    }

    public void setSishfinentrDlv(Date sishfinentrDlv) {
        this.sishfinentrDlv = sishfinentrDlv;
    }

    public String getSiscarDlv() {
        return siscarDlv;
    }

    public void setSiscarDlv(String siscarDlv) {
        this.siscarDlv = siscarDlv;
    }

    public String getSisstacardCli() {
        return sisstacardCli;
    }

    public void setSisstacardCli(String sisstacardCli) {
        this.sisstacardCli = sisstacardCli;
    }

    public BigDecimal getSisptosdtoCli() {
        return sisptosdtoCli;
    }

    public void setSisptosdtoCli(BigDecimal sisptosdtoCli) {
        this.sisptosdtoCli = sisptosdtoCli;
    }

    public BigDecimal getSismoneqvdtoptosCli() {
        return sismoneqvdtoptosCli;
    }

    public void setSismoneqvdtoptosCli(BigDecimal sismoneqvdtoptosCli) {
        this.sismoneqvdtoptosCli = sismoneqvdtoptosCli;
    }

    public BigDecimal getSisdtomaxptosCli() {
        return sisdtomaxptosCli;
    }

    public void setSisdtomaxptosCli(BigDecimal sisdtomaxptosCli) {
        this.sisdtomaxptosCli = sisdtomaxptosCli;
    }

    public DocumentosPago getSisforpagVen() {
        return sisforpagVen;
    }

    public void setSisforpagVen(DocumentosPago sisforpagVen) {
        this.sisforpagVen = sisforpagVen;
    }

    public DocumentosPago getSisforpagcrecliVen() {
        return sisforpagcrecliVen;
    }

    public void setSisforpagcrecliVen(DocumentosPago sisforpagcrecliVen) {
        this.sisforpagcrecliVen = sisforpagcrecliVen;
    }

    public TipoDocumentoIdentidad getSistidcod() {
        return sistidcod;
    }

    public void setSistidcod(TipoDocumentoIdentidad sistidcod) {
        this.sistidcod = sistidcod;
    }

    public Ubigeo getSisubicod() {
        return sisubicod;
    }

    public void setSisubicod(Ubigeo sisubicod) {
        this.sisubicod = sisubicod;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (siscod != null ? siscod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sistema)) {
            return false;
        }
        Sistema other = (Sistema) object;
        if ((this.siscod == null && other.siscod != null) || (this.siscod != null && !this.siscod.equals(other.siscod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Sistema[ siscod=" + siscod + " ]";
    }
    
}
