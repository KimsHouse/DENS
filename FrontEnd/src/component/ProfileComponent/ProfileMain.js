import React, { useState } from 'react'
import { Navigate, Outlet } from 'react-router-dom'
import ProfileInfo from './ProfileInfo'
import ProfileKeyword from './ProfileKeyword.js'
import { TagCloud } from 'react-tagcloud'
import { profileTest, profileUpdate } from '../../api/test'

export default function ProfileMain() {
  let [inputs, setInputs] = useState({
    image: '',
    name: '',
    position: '',
    stack: '',
    email: '',
    keyword: '',
    edit: false,
  })
  const { image, name, position, stack, email, edit } = inputs
  const keyword = [
    { value: 'JavaScript', count: 10000 },
    { value: 'React', count: 30 },
    { value: 'Nodejs', count: 28 },
    { value: 'Express.js', count: 25 },
    { value: 'HTML5', count: 33 },
    { value: 'MongoDB', count: 18 },
    { value: 'CSS3', count: 20 },
  ]
  const userId = email
  profileTest(
    userId,
    (res) => {
      setInputs({
        ...inputs,
        [image]: res.data.image,
        [name]: res.data.name,
        [position]: res.data.position,
        [stack]: res.data.stack,
        [email]: res.data.email,
      })
    },
    (error) => console.log(error)
  )
  profileUpdate(email, null, { params: { position, stack } })
  function onEdit() {
    setInputs({
      ...inputs,
      edit: !edit,
    })
  }
  return (
    <div>
      <h3>프로필 메인페이지입니다</h3>
      <TagCloud minSize={12} maxSize={35} tags={keyword} />

      <div>
        <img src={image} alt={name} />
        <p>이름: {name}</p>
        <p>직무:{edit ? <input></input> : position}</p>
        <p>스택:{edit ? <input></input> : stack}</p>
        <p>이메일: {email}</p>
        {edit ? <button>확인</button> : <button onClick={onEdit}>편집</button>}
      </div>
      <form>
        키워드를 입력해 주세요.
        <br />
        <input></input>
        <button>전송</button>
      </form>

      <Outlet />
    </div>
  )
}
