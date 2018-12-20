/*package cg.CodeSimplicity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CodeSimplicity {

	private static final String PageCrawlerImpl = null;


	public static void main(String[] args) {



	}

	public List<int[]> getThem( ) {
		List<int[]> list1 = new ArrayList<int[]>();
		for (int[] x : list1)
			if (x[0] == 4)
				list1.add(x);
		return list1;
	}

	public List<int[]> getFlaggedCells( ) {
		List<int[]> flaggedCells = new ArrayList<int[]>();
		//	int[] gameBoard1= null;
		for (int[] cell : flaggedCells){
			if (cell[STATUS_VALUE] == FLAGGED){
				flaggedCells.add(cell);

			}

		}
		return flaggedCells;
	}

	class DtaRcrd102 {
		private Date genymdhms;
		private Date modymdhms;
		private final String pszqint = "102";
		 ... 
	};
	//和
	class Customer {
		private Date generationTimestamp;private Date modificationTimestamp;;
		private final String recordId = "102";
		 ... 
	}



	public class Part {
		private String m_dsc; // The textual description
		void setName(String name) {
			m_dsc = name;
		}
	}
	//--------------------------------------------------------------------------------------
	public class Part {
		String description;
		void setDescription(String description) {
			this.description = description;
		}
	}



	private void printGuessStatistics(char candidate, int count) {
		String number;
		String verb;
		String pluralModifier;
		if (count == 0) {
			number = "no";
			verb = "are";
			pluralModifier = "s";
		} else if (count == 1) {
			number = "1";
			verb = "is";
			pluralModifier = "";
		} else {
			number = Integer.toString(count);
			verb = "are";
			pluralModifier = "s";
		}
		String guessMessage = String.format("There %s %s %s%s", verb, number, candidate, pluralModifier);
		// print(guessMessage);
	}


	private void printGuessStatistics(char candidate, int count) {
		String number;
		String verb;
		String pluralModifier;
		if (count == 0) {
			number = "no";
			verb = "are";
			pluralModifier = "s";
		} else if (count == 1) {
			number = "1";
			verb = "is";
			pluralModifier = "";
		} else {
			number = Integer.toString(count);
			verb = "are";
			pluralModifier = "s";
		}
		String guessMessage = String.format("There %s %s %s%s", verb, number, candidate, pluralModifier
				);
		print(guessMessage);
	}


	private void printGuessStatistics(char candidate, int count) {
		String number;
		String verb;
		String pluralModifier;
		if (count == 0) {
			number = "no";
			verb = "are";
			pluralModifier = "s";
		} else if (count == 1) {
			number = "1";
			verb = "is";
			pluralModifier = "";
		} else {
			number = Integer.toString(count);
			verb = "are";
			pluralModifier = "s";
		}
		String guessMessage = String.format("There %s %s %s%s", verb, number, candidate, pluralModifier
				);
		print(guessMessage);
	}



	public class GuessStatisticsMessage {
		private String number;
		private String verb;
		private String pluralModifier;

		public String make(char candidate, int count) {
			createPluralDependentMessageParts(count);
			return String.format(
					"There %s %s %s%s",
					verb, number, candidate, pluralModifier );
		}


		private void createPluralDependentMessageParts(int count) {
			if (count == 0) {
				thereAreNoLetters();
			} else if (count == 1) {
				thereIsOneLetter();
			} else {
				thereAreManyLetters(count);}
		}
		private void thereAreManyLetters(int count) {
			number = Integer.toString(count);
			verb = "are";
			pluralModifier = "s";
		}
		private void thereIsOneLetter() {
			number = "1";
			verb = "is";
			pluralModifier = "";
		}
		private void thereAreNoLetters() {
			number = "no";
			verb = "are";
			pluralModifier = "s";
		}
	}
	//面向对象的设计思想

	public static String testableHtml(PageData pageData,boolean includeSuiteSetup) throws Exception {
		WikiPage wikiPage = pageData.getWikiPage();
		StringBuffer buffer = new StringBuffer();
		if (pageData.hasAttribute("Test")) {
			if (includeSuiteSetup) {
				WikiPage suiteSetup =PageCrawlerImpl.getInheritedPage(SuiteResponder.SUITE_SETUP_NAME, wikiPage);
				if ( != null) {
					WikiPagePath pagePath =suiteSetup.getPageCrawler().getFullPath(suiteSetup);
					String pagePathName = PathParser.render(pagePath);
					buffer.append("!include -setup .")
					.append(pagePathName)
					.append("\n");
				}
			}
			WikiPage setup =PageCrawlerImpl.getInheritedPage("SetUp", wikiPage);
			if (setup != null) {

				WikiPagePath setupPath =	wikiPage.getPageCrawler().getFullPath(setup);
				String setupPathName = PathParser.render(setupPath);
				buffer.append("!include -setup .")
				.append(setupPathName)
				.append("\n");
			}
		}
		buffer.append(pageData.getContent());
		if (pageData.hasAttribute("Test")) {
			WikiPage teardown = PageCrawlerImpl.getInheritedPage("TearDown", wikiPage);
			if (teardown != null) {
				WikiPagePath tearDownPath =	wikiPage.getPageCrawler().getFullPath(teardown);
				String tearDownPathName = PathParser.render(tearDownPath);
				buffer.append("\n").append("!include -teardown .").append(tearDownPathName).append("\n");
			}
			if (includeSuiteSetup) {
				WikiPage suiteTeardown =PageCrawlerImpl.getInheritedPage(SuiteResponder.SUITE_TEARDOWN_NAME,wikiPage);
				if (suiteTeardown != null) {
					WikiPagePath pagePath =suiteTeardown.getPageCrawler().getFullPath (suiteTeardown);
					String pagePathName = PathParser.render(pagePath);
					buffer.append("!include -teardown .").append(pagePathName).append("\n");
				}
			}
		}
		pageData.setContent(buffer.toString());
		return pageData.getHtml();
	}


 * 团结协作,
 * 开发出高质量的软件. 
 * 培养自我的敬业精神,
 * 做一个有责任心的程序员,按照公司应有的代码规范来规范的书写代码,并且养成良好是代码习惯,努力学习提高代码质量.
 * 同时希望能和大家在未来的工作中相互分享,相互帮助,相互学习共同进步,一起为公司的发展奉献自己的绵薄之力.
 * 
 * 积极提高自己的代码质量.积极分享自己的 
 * 希望我能和部门内同事团结一致,大家携手共进,开发出更高质量的程序,为实现综合出行服务平台的早日实现共同努力.
 * 
 * 希望我能和部门内同事团结一致,大家携手共进,把综合出行服务平台做得更好,为给更多的公众提供智慧出行服务, 为给更多的客户提供满意的联网服出行产品而共同努力.
 * 
 * //  、下一阶段工作计划、希望得到的支持
 *  下一阶段工作计划, 首先,最主要的是保质保量的完成应有的开发计划和任务, 
 * //  其次,积极去深入了解行业发展状况和现阶段的市场需求,为日后新的开发需求做好知识储备. 
 *   深入研究原有的代码逻辑, 学习了解现有的业务逻辑, 以便于对接日后新的需求的开发.
 *  
 *  
 *  希望公司能把电脑,鼠标,键盘的硬件设备迭代升级一下..
 *  
 *  工作中遇到问题多沟通,及时解决问题. 时间过的好快都在公司工作了快三个多月了， 首先感谢公司领导和同事们给予我的鼓励和帮助，让我很快的融入到这个氛围很融洽的团队中，
 *  也很喜欢在这样的环境中工作。在这段时间的工作中，自己也在努力，也进步了不少，
 *  提升了很多新的技术，可以更好的围绕新的项目进行开发，我想这不仅是工作，更重要的是一次学习和锻炼能力的机会，能更好的提升自身的技术水平，
 *  为公司创造更大价值，同时为项目带来很多优秀的创意，
 *  更符合客户的需求体验。
 
	//=================================================================

	public static String renderPageWithSetupsAndTeardowns(
			PageData pageData, boolean isSuite
			) throws Exception {
		boolean isTestPage = pageData.hasAttribute("Test");
		if (isTestPage) {
			WikiPage testPage = pageData.getWikiPage();
			
			StringBuffer newPageContent = new StringBuffer();
			includeSetupPages(testPage, newPageContent, isSuite);
			newPageContent.append(pageData.getContent());
			includeTeardownPages(testPage, newPageContent, isSuite);
			pageData.setContent(newPageContent.toString());
		}
		return pageData.getHtml();
	}


	//======================================
}
*/