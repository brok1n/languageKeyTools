
#include "StringDataCenter.h"
#include "json/json.h"

USING_NS_CC;

CStringDataCenter * CStringDataCenter::m_pInstance = NULL;

CStringDataCenter::CStringDataCenter()
{
	m_sData = readFile(CONFIG_NAME);
}

CStringDataCenter::~CStringDataCenter()
{
	m_pInstance = NULL;
}

CStringDataCenter * CStringDataCenter::GetInstacne()
{
	if( !m_pInstance )
	{
		m_pInstance = new CStringDataCenter;
	}
	return m_pInstance;
}

std::string CStringDataCenter::readFile( std::string fileName )
{
	unsigned long size;
	char *pFileContent = (char*)CCFileUtils::sharedFileUtils()->getFileData(fileName.c_str(), "r", &size);

	if (pFileContent)
	{
		char *pCodes = new char[size + 1];
		pCodes[size] = '\0';
		memcpy(pCodes, pFileContent, size);
		delete[] pFileContent;

		std::string content = pCodes;
		return content;
	}

	return NULL;
}

std::string CStringDataCenter::get( std::string key, CONFIG_TYPE configType  )
{
	Json::Reader reader;
	Json::Value root;
	if(reader.parse(m_sData, root))
	{
		std::string type = configType == CONFIG_TYPE_CHINESE ? CHINESE : ENGLISH;
		std::string value = root[ DATA_LIST_KEY ][ key ][ type ].asString();

		return value;
	}
	
	return "";
}
