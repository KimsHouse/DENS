import { useEffect, useRef, useState, createContext } from 'react'
import { useCookies } from 'react-cookie'
import { useParams, useLocation } from 'react-router-dom'
import { API_BASE_URL } from '../../config'
// import SockJs from 'sockjs-client';
import * as StompJs from '@stomp/stompjs'
import SockJS from 'sockjs-client'
import MessageBox from './MessageBox'
import styled from 'styled-components'
import { store } from '../..'
import axios from 'axios'
import TeamInviteModal from './TeamInviteModal'

export default function MessageRoom() {
  // const yourId = useLocation().state.yourId
  const yourId = useLocation().state;
  console.log(yourId)

  const token = store.getState().user.token
  const profileId = store.getState().user.profileid

  const paramTest = useParams()
  const [cookie] = useCookies(['token'])
  const client = useRef({})
  const message = useRef()
  const [messagelog, setMessagelog] = useState([])
  const ref = useRef(null)

  function connect() {
    client.current = new StompJs.Client({
      webSocketFactory: () => new SockJS(`${API_BASE_URL}ws-stomp`),
      connectHeaders: {
        token: cookie.token,
      },
      onConnect: () => {
        client.current.subscribe(
          `/topic/chat/room/${paramTest.roomid}`,
          (message) => {
            const temp = JSON.parse(message.body)
            setMessagelog((messagelog) => [...messagelog, temp])
          }
        )
      },
      debug: function (str) {
        console.log(str)
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      onStompError: (frame) => {
        console.error(frame)
      },
    })
    client.current.activate()
  }
  const disconnect = () => {
    client.current.deactivate()
  }

  function send(e) {
    client.current.publish({
      destination: `/app/chat/${paramTest.roomid}`,
      body: JSON.stringify({
        type: 'TALK',
        roomId: paramTest.roomid,
        message: message.current.value,
      }),
      headers: { token: cookie.token },
    })
  }
  const [cookies] = useCookies()
  const authAxios = axios.create({
    baseURL: API_BASE_URL,
    headers: {
      Authorization: `Bearer "${cookies.token}"`,
      withCredentials: true,
    },
  })

  useEffect(() => {
    authAxios
      .get(`/chat/messages/${paramTest.roomid}`)
      .then((res) => {
        //console.log(res.data[0].sender);
        res.data.map((data) => {
          setMessagelog((messagelog) => [...messagelog, data])
        })
      })
      .catch((error) => {
        console.log(error)
      })
    connect()
  }, [])

  // ??? ???????????? ??????
  // 0. myId??? senderId??? ????????? senderId ??????
  // 1. ????????? ?????? (?????????) ?????? ????????????.
  // 2. ????????? ?????? ????????? ??????

  const [leaderTeams, setLeaderTeams] = useState()

  // ?????? ????????? ?????? ????????????
  const myleadersteam = () => {
    authAxios
      .get(`/team/leaderteam/${profileId}`)
      .then((response) => {
        setLeaderTeams(response.data)
      })
      .catch((fail) => {
        console.log(fail)
      })
  }

  useEffect(() => {
    // 1.
    {
      myleadersteam()
    }
  }, [profileId])

  console.log(messagelog)
  const [modal, setModal] = useState(false)
  const [teamId, setTeamId] = useState('')
  // const [theirId, setTheirId] = useState('');
  const modalOn = (theTeamId) => {
    setModal(true)
    setTeamId(theTeamId)
    // setTheirId(yourId);
  }
  const onCancel = () => {
    setModal(false)
  }
  const onConfirm = () => {
    setModal(false)
  }
  function EnterKey(e) {
    if (e.key === 'Enter') {
      send()
    }
  }

  const showTheTeams = ({ leaderTeams }) => {
    const result = []
    if (leaderTeams) {
      for (let i = 0; i < leaderTeams.length; i++) {
        let theTeamId = leaderTeams[i].team_id
        result.push(
          // <button onClick={inviteTeam({teamId, yourId})} key={i}>
          // </button>
          <div>
            <H5>{`${leaderTeams[i].title}`}</H5>
            <Btn onClick={() => {modalOn(theTeamId)}}>????????????</Btn>
          </div>
        )
      }
    }
    return result
  }

  return (
    <Container>
      {showTheTeams({ leaderTeams })}
      <TeamInviteModal
        visible={modal}
        onConfirm={onConfirm}
        onCancel={onCancel}
        teamId={teamId}
        yourId={yourId}
      />
      {/* <>
            <h3>?????????????????? ???</h3>
                { messagelog.map((data,idx) => (
                    <MessageBox key={idx } data={data}/>
                ))
            }
            <input name="text" ref={message}></input>


            <button onClick={send}>????????????</button>
        </> */}

      <ContainerT>
        <SectionName>?????????</SectionName>
        <SearchBigBox>
          {messagelog.map((data, key) => (
            <MessageBox
              data={data}
              key={key}
              profileId={profileId}
              yourId={yourId}
            />
          ))}
        </SearchBigBox>
        <MessageInputDiv>
          <InputBox name="text" ref={message} onKeyPress={EnterKey}></InputBox>
          <ButtonBox onClick={send}>??????</ButtonBox>
        </MessageInputDiv>
      </ContainerT>
    </Container>
  )
}

const Container = styled.div``
const ContainerT = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 50%;
`
const SectionName = styled.h3`
  font-family: 'Cafe24Ssurround';
  color: grey;
  text-shadow: 3px 3px #ccc;
  margin-top: 30px;
`
const MessageIndex = styled.div`
  overflow: scroll;
  width: 100%;
  height: 100%;
`

const MessageContainer = styled.div`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 50vw;
  height: 70vh;
`
const MessageInput = styled.input``
const MessageInputDiv = styled.div`
  display: flex;
  width: 37vw;
  height: 40px;
  color: #f46a72;
`
const SearchBigBox = styled.div`
  margin-left: 100px;
  display: flex;
  background-color: white;
  border: 1px solid pick;
  border-radius: 5px;
  //   box-shadow: 0px 30px 40px #f18fa1;
  flex-flow: column nowrap;
  width: 37vw;
  height: 60vh;
  margin: 20px;
  overflow: scroll;
  &::-webkit-scrollbar {
    display: none;
  }
`
const InputBox = styled.input`
  width: 85%;
`
const ButtonBox = styled.button`
  width: 20%;
  color: white;
  background-color: #f46a72;
`
const H5 = styled.span `
    color: #838383    
    margin: 10px;
    padding-top: 20px;
    font-family: 'Cafe24Ssurround';
`

const Btn = styled.button `
    border-radius: 5px;
    background: #06864D;
    color: white;
    border: none;
    font-size: 15px;
    font-family: 'Cafe24Ssurround';
    `