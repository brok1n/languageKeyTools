//
//  StringDataCenter.h
//  OneTwoThree
//
//  Created by wanglipeng on 14-4-16.
//
//

#ifndef __STRING_DATA_CENTER_H__
#define __STRING_DATA_CENTER_H__

#include "cocos2d.h"
#include <string>



class CStringDataCenter
{

#define CONFIG_NAME  "config.conf"
#define CHINESE    "chinese"
#define ENGLISH    "english"
#define DATA_LIST_KEY	"data"
#define KEY_LIST_KEY	"key"


	enum CONFIG_TYPE
	{
		CONFIG_TYPE_CHINESE = 0, 
		CONFIG_TYPE_ENGLISH
	};

public:
	CStringDataCenter();
	~CStringDataCenter();
    
private:
	std::string readFile( std::string );


public:
	std::string get( std::string key, CONFIG_TYPE configType = CONFIG_TYPE_CHINESE );
	static CStringDataCenter * GetInstacne();
	
private:
   std::string				m_sData;         //×Ö·û´®Êý¾Ý
   static CStringDataCenter *m_pInstance;

};

#endif /* defined(__STRING_DATA_CENTER_H__) */
