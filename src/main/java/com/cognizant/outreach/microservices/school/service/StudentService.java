/**
 * ${StudentService}
 *
 *  2019 Cognizant Technology Solutions. All Rights Reserved.
 *
 *  This software is the confidential and proprietary information of Cognizant Technology
 *  Solutions("Confidential Information").  You shall not disclose or use Confidential
 *  Information without the express written agreement of Cognizant Technology Solutions.
 *  Modification Log:
 *  -----------------
 *  Date                   Author           Description
 *  02/Mar/2019            371793        Developed base code structure
 *  ---------------------------------------------------------------------------
 */
package com.cognizant.outreach.microservices.school.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cognizant.outreach.microservices.school.vo.ClassVO;
import com.cognizant.outreach.microservices.school.vo.StudentSearchVO;
import com.cognizant.outreach.microservices.school.vo.TeamNameCountVO;

public interface StudentService {

	/**
	 * To list of team names and it's current count
	 * 
	 * @return List<TeamNameCountVO> 
	 */
	public List<TeamNameCountVO> getSchoolTeamList(long schoolId);

	
	/**
	 * To save or update students
	 * 
	 * @return ClassVO with updated id informations 
	 */
	public ClassVO saveStudents(ClassVO classVO);


	/**
	 * To download the excel template
	 * 
	 * 
	 * @param searchVO
	 * @param isExport true when the call is for student export
	 * @return
	 * @throws IOException 
	 */
	public byte[] downloadTemplate(StudentSearchVO searchVO, boolean isExport) throws IOException;


	/**
	 * Upload student data
	 * 
	 * @param file
	 * @param userId
	 * @return String returns the message if success else error message to be displayed
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public String uploadStudentData(MultipartFile file, String userId) throws ParseException, IOException;
	
	public static final String[] defaultTeamNames = {"aryabhata",
			"arvind joshi",
			"abhay ashtekar",
			"abhay bhushan",
			"aditi pant",
			"abdul kalam",
			"akhilesh k.gaharwar",
			"amar gupta",
			"anna mani",
			"avinash kak",
			"ashoke sen",
			"bhargav sri prakash",
			"birbal sahni",
			"brahmagupta",
			"biman bagchi",
			"bola vithal shetty",
			"c.mohan",
			"c.n.r.rao",
			"c.r.rao",
			"chanakya",
			"charaka",
			"c.v raman",
			"chitra mandal",
			"k.s.chandrasekharan",
			"charusita chakravarty",
			"d.bap reddy",
			"daya shankar kulshreshtha",
			"debasish ghose",
			"dipan ghosh",
			"dronamraju krishna rao",
			"g madhavan nair",
			"g.k.ananthasuresh",
			"giridhar madras",
			"g.n.ramachandran",
			"g.r.desiraju",
			"gaiti hasan",
			"gajendra pal singh raghava",
			"ganapathi thanikaimoni",
			"gandikota v.rao",
			"george sudarshan",
			"gomatam ravi",
			"govindarajan padmanabhan",
			"h.r.krishnamurthy",
			"halayudha",
			"har gobind khorana",
			"harish chandra",
			"himmatrao bawaskar",
			"homi jehangir bhabha",
			"jagdish chandra bose",
			"jagdish shukla",
			"jayant narlikar",
			"jogesh pati",
			"jyeshtharaj joshi",
			"k.r.ramanathan",
			"k.radhakrishnan",
			"k.sridhar",
			"kailas nath kaul",
			"kariamanickam srinivasa krishnan",
			"kavita shah",
			"kedareswar banerjee",
			"kewal krishan",
			"kotcherlakota rangadhama rao",
			"krishna ella",
			"krishnaswamy kasturirangan",
			"krityunjai prasad sinha",
			"m.g.k.menon",
			"m.l.madan",
			"m.s.swaminathan",
			"madhav gadgil",
			"madhava-kara",
			"mahavira",
			"man mohan sharma",
			"satyanarayana rao",
			"manilal bhaumik",
			"manindra agrawal",
			"mathukumalli vidyasagar",
			"meghnad saha",
			"michael lobo",
			"mirza faizan",
			"sir m.visvesvaraya",
			"manoj kumar jaiswal",
			"mylswamy annadurai",
			"mitali mukherji",
			"nagendra kumar singh",
			"nambi narayanan",
			"nandini harinath",
			"narendra karmarkar",
			"naresh dadhich",
			"narinder singh kapany",
			"nitya anand",
			"padmanabhan balaram",
			"pamposh bhat",
			"pandurang sadashiv khankhoje",
			"panini",
			"patcha ramachandra rao",
			"pingala",
			"pisharoth rama pisharoty",
			"prafulla chandra ray",
			"prahalad chunnilal vaidya",
			"pranav mistry",
			"prasanta chandra mahalanobis",
			"praveen kumar gorakavi",
			"prem chand pandey",
			"r.rajalakshmi",
			"raghunath anant mashelkar",
			"raj reddy",
			"raja ramanna",
			"rajeev motwani",
			"rajesh gopakumar",
			"ram chet chaudhary",
			"ranajit chakraborty",
			"rani bang",
			"ravi sankaran",
			"ravindra shripad kulkarni",
			"roddam narasimha",
			"ramesh raskar",
			"rohini godbole",
			"salim yusuf",
			"samir k.brahmachari",
			"sandip trivedi",
			"satish dhawan",
			"satish kumar",
			"satya churn law",
			"satyendra nath bose",
			"seema bhatnagar",
			"shankar abaji bhise",
			"shanti swarup bhatnagar",
			"shekhar c.mande",
			"shipra guha-mukherjee",
			"shiraz minwalla",
			"shivram bhoje",
			"shrinivas kulkarni",
			"shreeram shankar abhyankar",
			"siva s.banda",
			"srikumar banerjee",
			"srinivasa ramanujan",
			"subhash chandra lakhotia",
			"subhash kak",
			"subhash mukhopadhyay",
			"subramanyan chandrasekhar",
			"sujoy k.guha",
			"sunder lal hora",
			"sunil mukhi",
			"surendra nath pandeya",
			"suri bhagavantam",
			"sushanta kumar dattagupta",
			"sushruta",
			"swapan chattopadhyay",
			"va shiva ayyadurai",
			"s s pillai",
			"subramanian anantha ramakrishna",
			"t.v.ramakrishnan",
			"t.v.raman",
			"tapan misra",
			"tej p singh",
			"thanu padmanabhan",
			"u.aswathanarayana",
			"udipi ramachandra rao",
			"uma ramakrishnan",
			"uddhab bharali",
			"v.s.huzurbazar",
			"vagbhata",
			"varahamihira",
			"varun sahni",
			"vashishtha narayan singh",
			"veena parnaik",
			"venkatraman ramakrishnan",
			"vainu bappu",
			"vijay p.bhatkar",
			"vikram sarabhai",
			"vinod johri",
			"vasant ranchhod gowarikar",
			"vishnu vasudev narlikar",
			"yelavarthy nayudamma",
			"yellapragada subbarow"};
}
